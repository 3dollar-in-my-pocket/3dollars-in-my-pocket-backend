package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class TooManyRequestsException extends ThreeDollarsBaseException {

    public TooManyRequestsException(String message) {
        super(message, ErrorCode.TOO_MANY_REQUESTS);
    }

}
