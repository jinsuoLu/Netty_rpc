package com.selfdemo;

import com.selfdemo.discovery.Registry;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */
@Slf4j
public class ReferenceConfig<T> {
    private Class<T> interfaceRef;

    private Registry registry;


    /**
     * 代理设计模式,生成一个api接口代理对象
     * @return 代理对象
     */
    public T get() {
        // 此处一定是使用动态代理完成了一些工作
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class[] classes = new Class[]{interfaceRef};

        //使用动态代理生成代理对象
        Object helloProxy = Proxy.newProxyInstance(classLoader, classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 我们调用sayHi方法,事实上会走进这个代码段
                // 我们已经知道 method.args
                log.info("method-->{}",method.getName());
                log.info("args-->{}",args);

                // 1、发现服务，从注册中心，寻找一个可用的服务
                // 传入服务的名字,返回IP+端口
                InetSocketAddress addr = registry.lookup(interfaceRef.getName());
                if(log.isDebugEnabled()) {
                    log.debug("服务调用方发现了服务【{}】的可用主机【{}】",
                            interfaceRef.getName(),addr);
                }

                // 2、使用netty连接服务器，发送 调用的 服务的名字+方法名 + 参数列表，得到结果
                return null;
            }
        });

        return (T) helloProxy;
    }

    public Class<T> getInterfaceRef() {
        return interfaceRef;
    }

    public void setInterfaceRef(Class<T> interfaceRef) {
        this.interfaceRef = interfaceRef;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setInterface(Class<T> helloSelfRpcClass) {
        this.interfaceRef = helloSelfRpcClass;
    }
}
