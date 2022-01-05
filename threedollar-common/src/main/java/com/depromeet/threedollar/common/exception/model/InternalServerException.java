package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class InternalServerException extends ThreeDollarsBaseException {

    public InternalServerException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
