package com.depromeet.threedollar.common.model.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplicationStateChangedEvent {

    private static final String START_MESSAGE = "애플리케이션 서버가 시작됩니다";
    private static final String END_MESSAGE = "애플리케이션 서버가 종료됩니다";

    private final String applicationName;

    private final String message;

    private final LocalDateTime timeStamp;

    private final String applicationUid;

    @Builder(access = AccessLevel.PRIVATE)
    private ApplicationStateChangedEvent(String applicationName, String message, LocalDateTime timeStamp, String applicationUid) {
        this.applicationName = applicationName;
        this.message = message;
        this.timeStamp = timeStamp;
        this.applicationUid = applicationUid;
    }

    public static ApplicationStateChangedEvent start(String applicationName, LocalDateTime timeStamp, String applicationUid) {
        return ApplicationStateChangedEvent.builder()
            .applicationName(applicationName)
            .message(START_MESSAGE)
            .timeStamp(timeStamp)
            .applicationUid(applicationUid)
            .build();
    }

    public static ApplicationStateChangedEvent stop(String applicationName, LocalDateTime timeStamp, String applicationUid) {
        return ApplicationStateChangedEvent.builder()
            .applicationName(applicationName)
            .message(END_MESSAGE)
            .timeStamp(timeStamp)
            .applicationUid(applicationUid)
            .build();
    }

}
