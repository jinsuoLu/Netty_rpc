package com.selfdemo;

import com.selfdemo.impl.HelloSelfRpcImpl;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */

public class Application {

    public static void main(String[] args) {
        // 服务提供方,需要注册服务,启动服务
        // 1、封装要发布的服务
        ServiceConfig<HelloSelfRpc> service = new ServiceConfig<>();
        service.setInterface(HelloSelfRpc.class);
        service.setRef(new HelloSelfRpcImpl());
        // 2、定义注册中心

        // 3、通过启动引导程序，启动服务提供方
        //  （1） 配置 -- 应用的名称 -- 注册中心
        SelfRpcBootstrap.getInstance()
                .application("first-selfrpc-privider")
                // 配置注册中心
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .protocol(new ProtocolConfig("jdk"))
                // 发布服务
                .publish(service)
                // 启动服务
                .start();
    }

}
