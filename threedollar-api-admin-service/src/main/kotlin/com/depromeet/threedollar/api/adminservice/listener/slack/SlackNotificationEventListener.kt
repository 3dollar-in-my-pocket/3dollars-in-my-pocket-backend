package com.depromeet.threedollar.api.adminservice.listener.slack

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent
import com.depromeet.threedollar.infrastructure.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.infrastructure.external.client.slack.dto.request.PostSlackMessageRequest
import com.depromeet.threedollar.infrastructure.external.client.slack.type.SlackNotificationMessageType
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SlackNotificationEventListener(
    private val slackNotificationApiClient: SlackWebhookApiClient,
) {

    @EventListener
    fun sendInfoNotification(event: ApplicationStateChangedEvent) {
        slackNotificationApiClient.postMonitoringMessage(
            PostSlackMessageRequest.of(
                SlackNotificationMessageType.INFO_MESSAGE.generateMessage(
                    event.applicationType.description,
                    event.applicationUid,
                    event.message,
                    event.timeStamp
                )))
    }

}
