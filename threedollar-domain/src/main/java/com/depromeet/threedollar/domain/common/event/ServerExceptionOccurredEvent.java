package com.depromeet.threedollar.domain.common.event;

import com.depromeet.threedollar.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerExceptionOccurredEvent {

    private final ErrorCode errorCode;

    private final Exception exception;

    private final LocalDateTime timeStamp;

    public static ServerExceptionOccurredEvent error(ErrorCode errorCode, Exception exception, LocalDateTime timeStamp) {
        return new ServerExceptionOccurredEvent(errorCode, exception, timeStamp);
    }

}
