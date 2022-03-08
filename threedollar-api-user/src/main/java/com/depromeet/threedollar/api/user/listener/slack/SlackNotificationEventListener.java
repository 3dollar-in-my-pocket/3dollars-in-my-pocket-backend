package com.depromeet.threedollar.api.user.listener.slack;

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent;
import com.depromeet.threedollar.common.model.event.ServerExceptionOccurredEvent;
import com.depromeet.threedollar.external.client.slack.SlackWebhookApiClient;
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.ERROR_MESSAGE;
import static com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.INFO_MESSAGE;

@RequiredArgsConstructor
@Component
public class SlackNotificationEventListener {

    private final SlackWebhookApiClient slackNotificationApiClient;

    @Async
    @EventListener
    public void sendErrorNotification(ServerExceptionOccurredEvent event) {
        slackNotificationApiClient.postMessage(PostSlackMessageRequest.of(ERROR_MESSAGE.generateMessage(
            event.getApplicationType().getDescription(),
            event.getErrorCode().getCode(),
            event.getRequestUri(),
            event.getException(),
            event.getTimeStamp(),
            event.getErrorCode().getMessage(),
            event.getUserMetaValue()
        )));
    }

    @Async
    @EventListener
    public void sendInfoNotification(ApplicationStateChangedEvent event) {
        slackNotificationApiClient.postMessage(PostSlackMessageRequest.of(INFO_MESSAGE.generateMessage(
            event.getApplicationType().getDescription(),
            event.getMessage(),
            event.getTimeStamp()
        )));
    }

}