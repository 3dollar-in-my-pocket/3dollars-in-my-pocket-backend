package com.depromeet.threedollar.common.model.event;

import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationStateChangedEvent {

    private static final String START_MESSAGE = "애플리케이션 서버가 시작됩니다";
    private static final String END_MESSAGE = "애플리케이션 서버가 종료됩니다";

    private final ApplicationType applicationType;

    private final String message;

    private final LocalDateTime timeStamp;

    private final String applicationUid;

    public static ApplicationStateChangedEvent start(
        ApplicationType applicationType,
        LocalDateTime timeStamp,
        String applicationUid
    ) {
        return new ApplicationStateChangedEvent(applicationType, START_MESSAGE, timeStamp, applicationUid);
    }

    public static ApplicationStateChangedEvent stop(
        ApplicationType applicationType,
        LocalDateTime timeStamp,
        String applicationUid
    ) {
        return new ApplicationStateChangedEvent(applicationType, END_MESSAGE, timeStamp, applicationUid);
    }

}
