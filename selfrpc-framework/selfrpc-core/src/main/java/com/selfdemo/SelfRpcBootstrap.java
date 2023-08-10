package com.selfdemo;


import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */
@Slf4j
public class SelfRpcBootstrap {

    //SelfRpcBootstrap 是个单例,我们希望每个应用程序只有一个实例
    private static SelfRpcBootstrap selfRpcBootstrap = new SelfRpcBootstrap();

    private SelfRpcBootstrap() {
        //构造启动引导程序,需要做一些初始化的事
    }

    public static SelfRpcBootstrap getInstance() {
        return selfRpcBootstrap;
    }

    /**
     * 用来定义当前应用的名字
     * @param appName
     * @return
     */
    public SelfRpcBootstrap application(String appName) {
        return this;
    }


    /**
     * 用来配置一个注册中心
     * @param registryConfig
     * @return
     */
    public SelfRpcBootstrap registry(RegistryConfig registryConfig) {
        return this;
    }

    /**
     * 配置当前暴露的服务使用的协议
     * @param protocolConfig 协议的封装
     * @return
     */
    public SelfRpcBootstrap protocol(ProtocolConfig protocolConfig) {
        if(log.isDebugEnabled()) {
            log.debug("当前工程使用了:{}协议进行序列化",protocolConfig.toString());
        }
        return this;
    }

    /**
     * ------------------------------服务提供方的Api-----------------------------------
     */

    /**
     * 发布服务,将接口->实现,注册到服务中心
     * @param service 封装需要发布的服务
     * @return
     */
    public SelfRpcBootstrap publish(ServiceConfig<?> service) {
        if(log.isDebugEnabled()) {
            log.debug("当前工程使用了:{}服务进行发布",service.toString());
        }
        return this;
    }

    /**
     * 批量发布
     * @param services 封装需要发布的服务集合
     * @return
     */
    public SelfRpcBootstrap publish(List<?> services) {
        return this;
    }

    /**
     * 启动netty服务
     */
    public void start() {

    }


    /**
     * ------------------------------服务调用方的Api-----------------------------------
     */

    public SelfRpcBootstrap reference(ReferenceConfig<?> reference) {

        //在这个方法里我们是否可以拿到相关的配置项-注册中心
        //配置reference,将来调用get方法时,方便生成代理对象
        return this;
    }




}
