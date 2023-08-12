package com.selfdemo.exception;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */

public class ZookeeperException extends RuntimeException {
    public ZookeeperException(Throwable cause) {
        super(cause);
    }

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException() {
    }
}
