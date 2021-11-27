package com.depromeet.threedollar.domain.event;

import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.domain.domain.common.ServerEventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UnExpectedErrorOccurredEvent {

    private final ServerEventType type;

    private final ErrorCode errorCode;

    private final Exception exception;

    private final LocalDateTime timeStamp;

    public static UnExpectedErrorOccurredEvent error(ErrorCode errorCode, Exception exception, LocalDateTime timeStamp) {
        return new UnExpectedErrorOccurredEvent(ServerEventType.ERROR, errorCode, exception, timeStamp);
    }

}
