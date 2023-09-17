package com.kiosk.exception.impl;

import com.kiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class InfoOmissionException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "정보 입력이 잘못되거나, 누락되었습니다.";
    }
}
