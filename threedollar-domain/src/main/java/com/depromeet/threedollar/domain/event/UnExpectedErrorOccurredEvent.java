package com.depromeet.threedollar.domain.event;

import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.type.NotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UnExpectedErrorOccurredEvent {

    private final NotificationType type;

    private final ErrorCode errorCode;

    private final Exception exception;

    private final LocalDateTime timeStamp;

    public static UnExpectedErrorOccurredEvent error(ErrorCode errorCode, Exception exception, LocalDateTime timeStamp) {
        return new UnExpectedErrorOccurredEvent(NotificationType.ERROR, errorCode, exception, timeStamp);
    }

}
