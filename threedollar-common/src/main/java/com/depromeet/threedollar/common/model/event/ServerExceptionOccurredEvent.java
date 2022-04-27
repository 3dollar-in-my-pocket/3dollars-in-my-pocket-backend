package com.depromeet.threedollar.common.model.event;

import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.model.UserMetaValue;
import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ServerExceptionOccurredEvent {

    private final ApplicationType applicationType;

    private final ErrorCode errorCode;

    private final Exception exception;

    private final String requestUri;

    private final UserMetaValue userMetaValue;

    private final LocalDateTime timeStamp;

    @Builder(access = AccessLevel.PRIVATE)
    private ServerExceptionOccurredEvent(ApplicationType applicationType, ErrorCode errorCode, Exception exception,
                                         String requestUri, UserMetaValue userMetaValue, LocalDateTime timeStamp) {
        this.applicationType = applicationType;
        this.errorCode = errorCode;
        this.exception = exception;
        this.requestUri = requestUri;
        this.userMetaValue = userMetaValue;
        this.timeStamp = timeStamp;
    }

    public static ServerExceptionOccurredEvent error(ApplicationType applicationType, ErrorCode errorCode,
                                                     Exception exception, String requestUri, UserMetaValue userMetaValue, LocalDateTime timeStamp) {
        return ServerExceptionOccurredEvent.builder()
            .applicationType(applicationType)
            .errorCode(errorCode)
            .exception(exception)
            .requestUri(requestUri)
            .userMetaValue(userMetaValue)
            .timeStamp(timeStamp)
            .build();
    }

}
