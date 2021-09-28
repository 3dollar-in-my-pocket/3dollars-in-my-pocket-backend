package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public abstract class ThreeDollarsBaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public ThreeDollarsBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public boolean isSetAlarm() {
        return errorCode.isSetAlarm();
    }

    public int getStatus() {
        return errorCode.getStatus();
    }

}
