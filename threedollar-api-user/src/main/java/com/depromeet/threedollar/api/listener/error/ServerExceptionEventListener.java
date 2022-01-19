package com.depromeet.threedollar.api.listener.error;

import com.depromeet.threedollar.domain.common.event.ApplicationStateChangedEvent;
import com.depromeet.threedollar.domain.common.event.ServerExceptionOccurredEvent;
import com.depromeet.threedollar.external.client.slack.SlackApiClient;
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import io.sentry.Sentry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServerExceptionEventListener {

    private final SlackApiClient slackApiCaller;

    @Async
    @EventListener
    public void sendErrorNotification(ServerExceptionOccurredEvent event) {
        Sentry.captureException(event.getException());
        slackApiCaller.postMessage(PostSlackMessageRequest.of(String.format(NotificationMessageFormat.ERROR_MESSAGE.format,
            event.getErrorCode().getCode(),
            event.getTimeStamp(),
            event.getErrorCode().getMessage(),
            event.getException()
        )));
    }

    @Async
    @EventListener
    public void sendInfoNotification(ApplicationStateChangedEvent event) {
        slackApiCaller.postMessage(PostSlackMessageRequest.of(String.format(NotificationMessageFormat.INFO_MESSAGE.format,
            event.getTimeStamp(),
            event.getMessage()
        )));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private enum NotificationMessageFormat {

        ERROR_MESSAGE("[Error : %s]\nTimestamps: %s\nMessage: %s\nException: %s"),
        INFO_MESSAGE("[Info]\nTimestamps: %s\nMessage: %s"),
        ;

        private final String format;

    }

}
