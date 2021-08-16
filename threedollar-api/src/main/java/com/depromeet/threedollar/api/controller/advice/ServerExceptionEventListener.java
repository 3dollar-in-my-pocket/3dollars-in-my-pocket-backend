package com.depromeet.threedollar.api.controller.advice;

import com.depromeet.threedollar.api.event.ServerExceptionOccurredEvent;
import com.depromeet.threedollar.external.external.slack.SlackApiCaller;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServerExceptionEventListener {

    private static final String ERROR_MESSAGE_FORMAT = "[%s : %s]\nTimestamps: %s\nMessage: %s\nException: %s";

    private final SlackApiCaller slackApiCaller;

    @EventListener
    public void sendErrorNotification(ServerExceptionOccurredEvent event) {
        Sentry.captureException(event.getException());

        slackApiCaller.postMessage(String.format(ERROR_MESSAGE_FORMAT,
            event.getType(),
            event.getErrorCode().getCode(),
            event.getTimeStamp(),
            event.getErrorCode().getMessage(),
            event.getException()
        ));
    }

}
