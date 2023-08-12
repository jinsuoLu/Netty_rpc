package com.selfdemo.discovery.impl;

import com.selfdemo.Constant;
import com.selfdemo.ServiceConfig;
import com.selfdemo.discovery.AbstractRegistry;
import com.selfdemo.exception.DiscoveryException;
import com.selfdemo.utils.NetUtils;
import com.selfdemo.utils.ZookeeperNode;
import com.selfdemo.utils.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */
@Slf4j
public class ZookeeperRegistry extends AbstractRegistry {

    // 维护一个zookeeper实例
    private ZooKeeper zooKeeper;

    public ZookeeperRegistry(ZooKeeper zooKeeper) {
        this.zooKeeper = ZookeeperUtils.createZookeeper();
    }

    public ZookeeperRegistry(String connectString, int timeout) {
        this.zooKeeper = ZookeeperUtils.createZookeeper(connectString,timeout);
    }

    @Override
    public void register(ServiceConfig<?> service) {

        // 服务名称的节点
        String parentNode = Constant.BASE_PROVIDES_PATH+"/"+ service.getInterface().getName();
        // 这个节点应该是持久节点
        if(ZookeeperUtils.exists(zooKeeper,parentNode,null)) {
            ZookeeperNode zookeeperNode = new ZookeeperNode(parentNode,null);
            ZookeeperUtils.createNode(zooKeeper,zookeeperNode,null, CreateMode.PERSISTENT);
        }

        // 创建本机的临时节点，ip:port
        // 服务提供方的端口一般自行设定，我们还需要一个获取ip的方法
        // ip 我们通常需要一个局域网ip，不是本机127.0.0.1也不是ipv6
        //192.168.2.17
        //todo:后续处理端口
        String node = parentNode + "/" + NetUtils.getIp() + ":" + 8888;
        if(ZookeeperUtils.exists(zooKeeper,node,null)) {
            ZookeeperNode zookeeperNode = new ZookeeperNode(node,null);
            ZookeeperUtils.createNode(zooKeeper,zookeeperNode,null, CreateMode.EPHEMERAL);
        }
        if(log.isDebugEnabled()) {
            log.debug("服务{},已经被注册",service.getInterface().getName());
        }
    }

    @Override
    public InetSocketAddress lookup(String serviceName) {
        // 1、找到服务对应的节点
        String serviceNode = Constant.BASE_PROVIDES_PATH + "/"+ serviceName;

        log.info("节点名称【{}】",serviceNode);
        // 2、从zk中获取它的子节点，192.168.17.1：2151
        List<String> children = ZookeeperUtils.getChildren(zooKeeper,serviceNode,null);
        // 获取了所有的可用服务列表
        List<InetSocketAddress> collect = children.stream().map((ipString) -> {
            String[] ipsAndPort = ipString.split(":");
            String ip = ipsAndPort[0];
            int port = Integer.valueOf(ipsAndPort[1]);
            return new InetSocketAddress(ip, port);
        }).collect(Collectors.toList());

        if(collect.size()==0)
            throw new DiscoveryException("未发现可用服务主机");

        // todo q:每次调用相关方法的时候都需要去注册中心拉取服务列表吗？
        // 本地缓存+watcher   负载均衡策略
        //        我们如何合理的选择一个可用的服务而不是只获取第一个
        return collect.get(0);
    }
}
