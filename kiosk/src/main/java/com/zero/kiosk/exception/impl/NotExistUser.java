package com.zero.kiosk.exception.impl;

import com.zero.kiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistUser extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "유저정보가 존재하지 않습니다.";
    }
}
