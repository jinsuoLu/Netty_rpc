package com.selfdemo.exception;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */

public class NetworkException extends RuntimeException{

    public NetworkException() {
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
