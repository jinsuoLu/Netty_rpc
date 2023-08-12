package com.selfdemo;

import com.selfdemo.utils.ZookeeperNode;
import com.selfdemo.utils.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */
@Slf4j
public class Application {

    public static void main(String[] args) {
        //创建一个zookeeper实例
        ZooKeeper zookeeper = ZookeeperUtils.createZookeeper();

        String basePath = "/selfRpc-metadata";
        String providerPath = basePath+"/providers";
        String consumerPath = basePath+"/consumers";

        ZookeeperNode baseNode = new ZookeeperNode("/selfRpc-metadata",null);
        ZookeeperNode providerNode = new ZookeeperNode(providerPath,null);
        ZookeeperNode consumerNode = new ZookeeperNode(consumerPath,null);

        List.of(baseNode,providerNode,consumerNode).forEach(node -> ZookeeperUtils.createNode(zookeeper,node,null,CreateMode.PERSISTENT));

        ZookeeperUtils.close(zookeeper);

    }


}
