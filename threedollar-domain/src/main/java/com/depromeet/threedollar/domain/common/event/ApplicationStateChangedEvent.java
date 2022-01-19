package com.depromeet.threedollar.domain.common.event;

import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationStateChangedEvent {

    private static final String START_MESSAGE = "애플리케이션 서버가 시작합니다";
    private static final String END_MESSAGE = "애플리케이션 서버가 종료합니다";

    private final ApplicationType applicationType;

    private final String message;

    private final LocalDateTime timeStamp;

    public static ApplicationStateChangedEvent start(ApplicationType applicationType, LocalDateTime timeStamp) {
        return new ApplicationStateChangedEvent(applicationType, START_MESSAGE, timeStamp);
    }

    public static ApplicationStateChangedEvent stop(ApplicationType applicationType, LocalDateTime timeStamp) {
        return new ApplicationStateChangedEvent(applicationType, END_MESSAGE, timeStamp);
    }

}
