package com.depromeet.threedollar.api.listener.slack;

import com.depromeet.threedollar.domain.common.event.ApplicationStateChangedEvent;
import com.depromeet.threedollar.domain.common.event.ServerExceptionOccurredEvent;
import com.depromeet.threedollar.external.client.slack.SlackApiClient;
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.ERROR_MESSAGE;
import static com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.INFO_MESSAGE;

@RequiredArgsConstructor
@Component
public class SlackNotificationEventListener {

    private final SlackApiClient slackApiCaller;

    @Async
    @EventListener
    public void sendErrorNotification(ServerExceptionOccurredEvent event) {
        Sentry.captureException(event.getException());

        slackApiCaller.postMessage(PostSlackMessageRequest.of(ERROR_MESSAGE.generateMessage(
            event.getApplicationType().getDescription(),
            event.getErrorCode().getCode(),
            event.getTimeStamp(),
            event.getErrorCode().getMessage(),
            event.getException()
        )));
    }

    @Async
    @EventListener
    public void sendInfoNotification(ApplicationStateChangedEvent event) {
        slackApiCaller.postMessage(PostSlackMessageRequest.of(INFO_MESSAGE.generateMessage(
            event.getApplicationType().getDescription(),
            event.getTimeStamp(),
            event.getMessage()
        )));
    }

}
