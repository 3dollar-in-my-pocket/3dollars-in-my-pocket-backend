package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class ConflictException extends ThreeDollarsBaseException {

    public ConflictException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ConflictException(String message) {
        super(message, ErrorCode.CONFLICT_EXCEPTION);
    }

}
