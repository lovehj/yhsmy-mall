package com.yhsmy.exception;

/**
 * 自定义异常处理类
 *
 * @auth 李正义
 * @date 2019/11/8 9:46
 **/
public class ServiceException extends RuntimeException {
    private  String message;

    public  ServiceException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
