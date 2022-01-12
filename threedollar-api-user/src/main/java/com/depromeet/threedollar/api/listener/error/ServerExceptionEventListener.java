package com.depromeet.threedollar.api.listener.error;

import com.depromeet.threedollar.domain.common.event.UnExpectedErrorOccurredEvent;
import com.depromeet.threedollar.external.client.slack.SlackApiClient;
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServerExceptionEventListener {

    private static final String ERROR_MESSAGE_FORMAT = "[%s : %s]\nTimestamps: %s\nMessage: %s\nException: %s";

    private final SlackApiClient slackApiCaller;

    @Async
    @EventListener
    public void sendErrorNotification(UnExpectedErrorOccurredEvent event) {
        Sentry.captureException(event.getException());

        slackApiCaller.postMessage(PostSlackMessageRequest.of(String.format(ERROR_MESSAGE_FORMAT,
            event.getType(),
            event.getErrorCode().getCode(),
            event.getTimeStamp(),
            event.getErrorCode().getMessage(),
            event.getException()
        )));
    }

}
