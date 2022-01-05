package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class ForbiddenException extends ThreeDollarsBaseException {

    public ForbiddenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN_EXCEPTION);
    }

}
