package com.selfdemo.discovery;

import com.selfdemo.Constant;
import com.selfdemo.discovery.impl.ZookeeperRegistry;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */

public class RegistryConfig {

    // 定义连接的 url zookeeper://127.0.0.1:2181  Redis
    private String connectString;

    public RegistryConfig(String connectString) {
        this.connectString = connectString;
    }

    /**
     * 使用简单工厂模式实现
     * @return
     */
    public Registry getRegistry() {
        // 1、获取注册中心的类型
        String registryType = getRegistryType(connectString,true).toLowerCase().trim();
        if(registryType.equals("zookeeper")) {
            String host = getRegistryType(connectString,false);
            return new ZookeeperRegistry(host, Constant.TIME_OUT);
        }//todo: 可以扩展其他中间件

        return null;
        // return new DiscoveryException("未发现合适的注册中心");
    }

    private String getRegistryType(String connectString,boolean ifType) {
        String[] typeAndHost = connectString.split("://");
        if(typeAndHost.length != 2) {
            throw new RuntimeException("给定的注册中心连接url不合法");
        }
        if(ifType) {
            return typeAndHost[0];
        }
        else {
            return typeAndHost[1];
        }
    }
}
