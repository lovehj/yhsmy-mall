package com.yhsmy.exception;

/**
 * @auth 李正义
 * @date 2019/11/24 14:42
 **/
public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException () {
        super ();
    }

    public PageNotFoundException (String message, Throwable cause) {
        super (message, cause);
    }

    public PageNotFoundException (String message) {
        super (message);
    }

    public PageNotFoundException (Throwable cause) {
        super (cause);
    }
}
