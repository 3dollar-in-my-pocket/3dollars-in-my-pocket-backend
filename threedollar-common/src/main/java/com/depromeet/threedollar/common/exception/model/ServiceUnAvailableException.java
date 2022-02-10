package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class ServiceUnAvailableException extends ThreeDollarsBaseException {

    public ServiceUnAvailableException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ServiceUnAvailableException(String message) {
        super(message, ErrorCode.SERVICE_UNAVAILABLE);
    }

}
