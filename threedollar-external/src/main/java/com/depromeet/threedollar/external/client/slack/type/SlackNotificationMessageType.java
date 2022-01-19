package com.depromeet.threedollar.external.client.slack.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SlackNotificationMessageType {

    ERROR_MESSAGE("[%s - Error : %s]\nTimestamps: %s\nMessage: %s\nException: %s"),
    INFO_MESSAGE("[%s - Info]\nTimestamps: %s\nMessage: %s"),
    ;

    private final String template;

    public String generateMessage(Object... args) {
        return String.format(template, args);
    }

}
