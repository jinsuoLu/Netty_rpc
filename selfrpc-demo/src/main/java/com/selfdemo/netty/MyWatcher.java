package com.selfdemo.netty;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Date:2023/8/9
 * Author:ljs
 * Description:
 */

public class MyWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        //判断事件类型,连接类型的事件
        if(event.getType()== Event.EventType.None) {
            if(event.getState()==Event.KeeperState.SyncConnected) {
                System.out.println("zookeeper连接成功");
            } else if(event.getState()==Event.KeeperState.AuthFailed) {
                System.out.println("zookeeper认证失败");
            } else if(event.getState()==Event.KeeperState.Disconnected) {
                System.out.println("zookeeper连接失败");
            }
        } else if(event.getType()==Event.EventType.NodeCreated) {
            System.out.println(event.getPath()+"被创建了");
        } else if(event.getType()==Event.EventType.NodeDeleted) {
            System.out.println(event.getPath()+"被删除了");
        } else if(event.getType()==Event.EventType.NodeDataChanged) {
            System.out.println(event.getPath()+"数据被改变了");
        } else if(event.getType()==Event.EventType.NodeChildrenChanged) {
            System.out.println(event.getPath()+"子节点被改变了");
        }
    }
}
