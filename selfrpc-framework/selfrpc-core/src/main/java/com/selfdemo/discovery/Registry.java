package com.selfdemo.discovery;

import com.selfdemo.ServiceConfig;

import java.net.InetSocketAddress;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:注册中心具有的能力
 */

public interface Registry {

    /**
     * 注册服务
     * @param serverConfig 服务的配置内容
     */
    void register(ServiceConfig<?> serverConfig);

    /**
     * 从注册中心拉取一个可用的服务
     *
     * @param serviceName 服务的名称
     * @return 服务的地址
     */

    InetSocketAddress lookup(String serviceName);
}
