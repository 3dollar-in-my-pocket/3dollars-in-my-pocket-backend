package com.depromeet.threedollar.domain.common.event;

import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.model.UserMetaValue;
import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerExceptionOccurredEvent {

    private final ApplicationType applicationType;

    private final ErrorCode errorCode;

    private final Exception exception;

    private final UserMetaValue userMetaValue;

    private final LocalDateTime timeStamp;

    public static ServerExceptionOccurredEvent error(ApplicationType applicationType, ErrorCode errorCode, Exception exception, UserMetaValue userMetaValue, LocalDateTime timeStamp) {
        return new ServerExceptionOccurredEvent(applicationType, errorCode, exception, userMetaValue, timeStamp);
    }

}
