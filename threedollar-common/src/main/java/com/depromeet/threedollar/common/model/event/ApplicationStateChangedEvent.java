package com.depromeet.threedollar.common.model.event;

import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplicationStateChangedEvent {

    private static final String START_MESSAGE = "애플리케이션 서버가 시작됩니다";
    private static final String END_MESSAGE = "애플리케이션 서버가 종료됩니다";

    private final ApplicationType applicationType;

    private final String message;

    private final LocalDateTime timeStamp;

    private final String applicationUid;

    @Builder(access = AccessLevel.PRIVATE)
    private ApplicationStateChangedEvent(ApplicationType applicationType, String message, LocalDateTime timeStamp, String applicationUid) {
        this.applicationType = applicationType;
        this.message = message;
        this.timeStamp = timeStamp;
        this.applicationUid = applicationUid;
    }

    public static ApplicationStateChangedEvent start(ApplicationType applicationType, LocalDateTime timeStamp, String applicationUid) {
        return ApplicationStateChangedEvent.builder()
            .applicationType(applicationType)
            .message(START_MESSAGE)
            .timeStamp(timeStamp)
            .applicationType(applicationType)
            .applicationUid(applicationUid)
            .build();
    }

    public static ApplicationStateChangedEvent stop(ApplicationType applicationType, LocalDateTime timeStamp, String applicationUid) {
        return ApplicationStateChangedEvent.builder()
            .applicationType(applicationType)
            .message(END_MESSAGE)
            .timeStamp(timeStamp)
            .applicationType(applicationType)
            .applicationUid(applicationUid)
            .build();
    }

}
