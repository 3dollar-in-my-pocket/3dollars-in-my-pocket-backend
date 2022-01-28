package com.depromeet.threedollar.external.client.slack.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SlackNotificationMessageType {

    ERROR_MESSAGE("[%s - Error : %s]\nTimestamps: %s\nMeta: %s\nMessage: %s\nException: %s"),
    INFO_MESSAGE("[%s - Info]\nTimestamps: %s\nMessage: %s"),
    NEW_BOSS_REGISTRATION_MESSAGE("새로운 사장님이 가입 신청하였습니다.\n성함: %s\n가게이름: %s\n연락처: %s"),
    ;

    private final String template;

    public String generateMessage(Object... args) {
        return String.format(template, args);
    }

}
