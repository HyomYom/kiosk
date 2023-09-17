package com.kiosk.exception.impl;

import com.kiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistStore extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 상점입니다.";
    }
}
