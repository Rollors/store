package com.cy.store.service.ex;

// 用户在更新数据时产生未知的异常
public class UpadateException extends ServiceException{
    public UpadateException() {
        super();
    }

    public UpadateException(String message) {
        super(message);
    }

    public UpadateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpadateException(Throwable cause) {
        super(cause);
    }

    protected UpadateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
