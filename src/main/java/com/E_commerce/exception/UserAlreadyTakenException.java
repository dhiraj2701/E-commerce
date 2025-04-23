package com.E_commerce.exception;

import com.E_commerce.enums.ErrorCode;

import java.io.Serializable;
import java.util.Map;

public class UserAlreadyTakenException extends BaseException {

    public UserAlreadyTakenException() {
        super();
    }

    public UserAlreadyTakenException(Map<String, Serializable> data) {
        super(data);
    }

    public UserAlreadyTakenException(Throwable cause, Map<String, Serializable> data) {
        super(cause, data);
    }

    @Override
    public ErrorCode getCode() {
        return ErrorCode.USERNAME_ALREADY_TAKEN;
    }
}

