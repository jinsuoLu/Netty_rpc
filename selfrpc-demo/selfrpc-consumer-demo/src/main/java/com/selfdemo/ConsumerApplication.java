package com.selfdemo;

import com.selfdemo.discovery.RegistryConfig;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */

public class ConsumerApplication {
    public static void main(String[] args) {
        // 获取代理对象,使用ReferenceConfig进行封装
        // reference 一定用生成代理的模板方法，get（）
        ReferenceConfig<HelloSelfRpc> reference = new ReferenceConfig<>();
        reference.setInterface(HelloSelfRpc.class);


        // 代理做了什么：
        // 1、连接注册中心
        // 2、拉去服务列表
        // 3、选择一个服务并建立连接
        // 4、发送请求，携带一些信息（接口名，参数列表，方法名），获得结果
        SelfRpcBootstrap.getInstance()
                .application("first-selfRpc-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference);
        // 获取一个代理对象
        HelloSelfRpc helloSelfRpc = reference.get();
        helloSelfRpc.sayHi("hello");
    }
}
