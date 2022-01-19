package com.depromeet.threedollar.external.client.slack.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SlackNotificationMessageType {

    ERROR_MESSAGE("[Error : %s]\nTimestamps: %s\nMessage: %s\nException: %s"),
    INFO_MESSAGE("[Info]\nTimestamps: %s\nMessage: %s"),
    ;

    private final String template;

    public String generateMessage(Object... args) {
        return String.format(template, args);
    }

}
