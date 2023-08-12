package com.selfdemo;


import com.selfdemo.discovery.Registry;
import com.selfdemo.discovery.RegistryConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */
@Slf4j
public class SelfRpcBootstrap {

    //SelfRpcBootstrap 是个单例,我们希望每个应用程序只有一个实例
    private static final SelfRpcBootstrap selfRpcBootstrap = new SelfRpcBootstrap();

    // 维护一个zookeeper实例
    // private ZooKeeper zooKeeper;

    // 定义相关的一些基础配置
    private String appName = "default";
    private RegistryConfig registryConfig;
    private ProtocolConfig protocolConfig;
    private int port = 8888;

    //注册中心
    private Registry registry;

    // 芜湖已经发布且暴露的服务列表，key -> interface的权限 value -> ServiceConfig
    private static Map<String,ServiceConfig<?>> SERVER_LIST = new HashMap<>(16);

    
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
        this.appName = appName;
        return this;
    }


    /**
     * 用来配置一个注册中心
     * @param registryConfig
     * @return
     */
    public SelfRpcBootstrap registry(RegistryConfig registryConfig) {
        // 这里维护一个zookeeper实例，但是，如果这样写就会将zookeeper和当前工程耦合
        // 我们其实更希望以后可以扩展
        this.registry = registryConfig.getRegistry();
        return this;
    }

    /**
     * 配置当前暴露的服务使用的协议
     * @param protocolConfig 协议的封装
     * @return
     */
    public SelfRpcBootstrap protocol(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
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
        // 抽象了注册中心的概念,使用注册中心的一个实现完成注册
        registry.register(service);

        // 1、当服务调用方，通过接口、方法名、具体方法参数列表发起调用，提供方怎么知道使用哪一个实现
        //  （1）new 一个 （2）spring beanFactory.getBean(Class) （3）自己维护映射关系
        SERVER_LIST.put(service.getInterface().getName(),service);
        return this;
    }

    /**
     * 批量发布
     * @param services 封装需要发布的服务集合
     * @return
     */
    public SelfRpcBootstrap publish(List<ServiceConfig<?>> services) {

        for (ServiceConfig<?> service : services) {
            this.publish(service);
        }
        return this;
    }

    /**
     * 启动netty服务
     */
    public void start() {

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * ------------------------------服务调用方的Api-----------------------------------
     */

    public SelfRpcBootstrap reference(ReferenceConfig<?> reference) {

        //在这个方法里我们是否可以拿到相关的配置项-注册中心
        //配置reference,将来调用get方法时,方便生成代理对象
        // 1、reference需要一个注册中心
        reference.setRegistry(registry);
        // registryConfig.
        return this;
    }




}
