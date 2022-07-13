package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class InternalServerException extends ThreeDollarsBaseException {

    public InternalServerException(String message) {
        super(message, ErrorCode.E500_INTERNAL_SERVER);
    }

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
