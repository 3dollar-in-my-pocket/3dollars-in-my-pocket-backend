package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class InvalidException extends ThreeDollarsBaseException {

    public InvalidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidException(String message) {
        super(message, ErrorCode.INVALID);
    }

}
