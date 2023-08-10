package com.selfdemo.impl;

import com.selfdemo.HelloSelfRpc;

/**
 * Date:2023/8/10
 * Author:ljs
 * Description:
 */

public class HelloSelfRpcImpl implements HelloSelfRpc {
    @Override
    public String sayHi(String msg) {
        return "hi consumer:"+msg;
    }
}
