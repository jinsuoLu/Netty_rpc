package com.selfdemo;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */

public class Constant {

    // zookeeper 的默认连接地址
    public static final String DEFAULT_ZK_CONNECT = "127.0.0.1:2181";

    // zookeeper 的默认超时时间
    public static final int TIME_OUT = 10000;

    //服务提供方和调用方 在注册中心的基础路径
    public static final String BASE_PROVIDES_PATH = "/selfRpc-metadata/providers";
    public static final String BASE_CONSUMERS_PATH = "/selfRpc-metadata/consumers";
}
