package com.selfdemo.exception;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */

public class DiscoveryException extends RuntimeException {
    public DiscoveryException() {
    }

    public DiscoveryException(String message) {
        super(message);
    }

    public DiscoveryException(Throwable cause) {
        super(cause);
    }
}
