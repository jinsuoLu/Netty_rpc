package com.selfdemo.utils;

import com.selfdemo.Constant;
import com.selfdemo.exception.ZookeeperException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */
@Slf4j
public class ZookeeperUtils {

    /**
     * 使用默认配置创建zookeeper实例
     * @return zookeeper 实例
     */
    public static ZooKeeper createZookeeper() {
        //定义连接参数
        String connectString = Constant.DEFAULT_ZK_CONNECT;
        //定义超时时间
        int timeout = Constant.TIME_OUT;

        return createZookeeper(connectString,timeout);

    }

    public static ZooKeeper createZookeeper(String connectString, int timeout) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            // 创建zookeeper实例,建立连接
            final ZooKeeper zooKeeper = new ZooKeeper(connectString, timeout, event -> {
                //只有连接成功才放行
                if(event.getState()== Watcher.Event.KeeperState.SyncConnected) {
                    System.out.println("客户端连接成功");
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            return zooKeeper;
        } catch (IOException | InterruptedException e) {
            log.error("创建zookeeper实例时发生异常:{}",e);
            //throw new ZookeeperException();
        }
        return null;
    }


    /**
     * 创建一个节点的工具方法
     * @param zookeeper zk实例
     * @param node 节点
     * @param watcher watcher实例
     * @param createMode
     * @return true: 成功创建  false: 节点已经存在
     */
    public  static Boolean createNode(ZooKeeper zookeeper, ZookeeperNode node, Watcher watcher, CreateMode createMode) {
        try {
            if(zookeeper.exists(node.getNodePath(),watcher)==null) {
                String result = zookeeper.create(node.getNodePath(), node.getData(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                log.info("根节点【{}】,成功创建",result);
                return true;
            } else {
                if(log.isDebugEnabled()) {
                    log.info("根节点【{}】，已经存在，无需创建",node.getNodePath());
                }
                return false;
            }
        } catch (KeeperException | InterruptedException e) {
            log.error("创建基础目录时发生异常：",e);
            throw new ZookeeperException();
        }
    }

    /**
     * 关闭zk的方法
     * @param zk zookeeper实例
     */
    public static void close(ZooKeeper zk) {
        try {
            zk.close();
        } catch (InterruptedException e) {
            log.error("关闭zk时发生异常：",e);
            throw new ZookeeperException();
        }
    }

    /**
     * 判断节点是否存在
     * @param zk zookeeper实例
     * @param node 节点路径
     * @param watcher watcher
     * @return true：存在 | false：不存在
     */
    public static boolean exists(ZooKeeper zk,String node, Watcher watcher) {
        try {
            return zk.exists(node,watcher)==null;
        } catch (KeeperException | InterruptedException e) {
            log.error("判断节点[{}]是否存在发生异常",node,e);
            throw new ZookeeperException();
        }
    }

    /**
     * 查询一个节点的子元素
     * @param zooKeeper zk实例
     * @param serviceNode 服务节点
     * @return 子元素列表
     */
    public static List<String> getChildren(ZooKeeper zooKeeper, String serviceNode, Watcher watcher) {

        try {
            List<String> children = zooKeeper.getChildren(serviceNode, watcher);
            return children;
        } catch (KeeperException | InterruptedException e) {
            log.error("获取节点【{}】的子元素时发生异常",serviceNode,e);
            throw new ZookeeperException(e);
        }

    }
}
