package com.depromeet.threedollar.common.exception.model;

import com.depromeet.threedollar.common.exception.type.ErrorCode;

public class UnAuthorizedException extends ThreeDollarsBaseException {

    public UnAuthorizedException(String message) {
        super(message, ErrorCode.E401_UNAUTHORIZED);
    }

}
