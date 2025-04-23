package com.E_commerce.exception;

import com.E_commerce.enums.ErrorCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public abstract class BaseException extends RuntimeException{
    private final Map<String, Serializable> data;

    protected BaseException() {
        super();
        this.data = null;
    }

    protected BaseException(Throwable cause) {
        super(cause);
        this.data = null;
    }

    protected BaseException(Map<String, Serializable> data) {
        super();
        this.data = data;
    }

    protected BaseException( Throwable cause, Map<String, Serializable> data) {
        super(cause);
        this.data = data;
    }

    public abstract ErrorCode getCode();

    @Override
    public String getMessage(){
        String message = this.getCode().getMessage();
        if(this.getCause() != null) {
            message = message + ": " + this.getCause().getMessage();
        }
        return message;
    }
}
