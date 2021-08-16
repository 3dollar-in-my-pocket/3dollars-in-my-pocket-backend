package com.depromeet.threedollar.api.event;

import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.common.type.ServerEventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerExceptionOccurredEvent {

    private final ServerEventType type;

    private final ErrorCode errorCode;

    private final Exception exception;

    private final LocalDateTime timeStamp;

    public static ServerExceptionOccurredEvent error(ErrorCode errorCode, Exception exception, LocalDateTime timeStamp) {
        return new ServerExceptionOccurredEvent(ServerEventType.ERROR, errorCode, exception, timeStamp);
    }

}
