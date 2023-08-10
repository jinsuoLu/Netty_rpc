package com.selfdemo;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */

public interface HelloSelfRpc {

    /**
     * 通用接口,server 和 client都需要依赖
     * @param msg 发送的具体消息
     * @return 返回的结果
     */
    String sayHi(String msg);
}
