package com.depromeet.threedollar.common.type;

import lombok.Getter;

@Getter
public enum HttpStatusCode {

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    CONFLICT(409),
    UNSUPPORTED_MEDIA_TYPE(415),
    TOO_MANY_REQUESTS(429),
    INTERNAL_SERVER(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    ;

    private final int status;

    HttpStatusCode(int status) {
        this.status = status;
    }

}
