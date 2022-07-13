package com.depromeet.threedollar.common.exception.model;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E502_BAD_GATEWAY;

public class BadGatewayException extends ThreeDollarsBaseException {

    public BadGatewayException(String message) {
        super(message, E502_BAD_GATEWAY);
    }

}
