package com.selfdemo;

import com.selfdemo.netty.MyWatcher;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Date:2023/8/9
 * Author:ljs
 * Description:
 */

public class ZookeeperTest {

    ZooKeeper zooKeeper;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Before
    public void createZk() {

        //定义连接参数
        String connectString = "127.0.0.1:2181";
        //定义超时时间
        int timeout = 10000;

        try {
            //new MyWatcher() 默认的Watcher
            zooKeeper = new ZooKeeper(connectString, timeout, event -> {
                if(event.getState()== Watcher.Event.KeeperState.SyncConnected) {
                    System.out.println("客户端连接成功");
                    countDownLatch.countDown();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreatePNode() {
        try {
            //等待连接成功
            countDownLatch.await();
            String result = zooKeeper.create("/ydlclass", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(result);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(zooKeeper!=null)
                    zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testDeletePNode() {
        try {
            //version : cas,mysql 乐观锁,无视版本号
            zooKeeper.delete("/ydlclass",-1);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(zooKeeper!=null)
                    zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testExistsPNode() {
        try {
            //version : cas,mysql 乐观锁,无视版本号
            Stat stat = zooKeeper.exists("/ljsclass", null);

            zooKeeper.setData("/ljsclass","h1".getBytes(),-1);
            //当前节点的数据版本
            int version = stat.getVersion();
            System.out.println("version="+version);
            // 当前节点的acl数据版本
            int aversion = stat.getAversion();
            System.out.println("aversion="+aversion);
            //当前子节点的数据版本
            int cversion = stat.getCversion();
            System.out.println("cversion="+cversion);

        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(zooKeeper!=null)
                    zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testWatcher() {
        try {
            //注册Watcher,也可以直接new一个新的Watcher
            //可以使用true使用默认Watcher
            zooKeeper.exists("/ljsclass", true);
            //zookeeper.getChildren();
            //zookeeper.getData();
            while (true)
            Thread.sleep(Integer.MAX_VALUE);

        } catch (KeeperException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(zooKeeper!=null)
                    zooKeeper.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
