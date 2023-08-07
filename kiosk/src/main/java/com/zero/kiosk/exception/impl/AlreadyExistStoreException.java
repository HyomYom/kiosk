package com.zero.kiosk.exception.impl;

import com.zero.kiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistStoreException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 등록된 상점입니다.";
    }
}
