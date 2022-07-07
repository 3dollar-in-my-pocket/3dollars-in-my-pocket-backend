package com.depromeet.threedollar.api.core.listener.commonservice.slack

import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent
import com.depromeet.threedollar.common.model.event.ServerExceptionOccurredEvent
import com.depromeet.threedollar.common.type.template.SlackMessageTemplateType
import com.depromeet.threedollar.infrastructure.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.infrastructure.external.client.slack.dto.request.PostSlackMessageRequest
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CommonSlackNotificationEventListener(
    private val slackNotificationApiClient: SlackWebhookApiClient,
) {

    @Async
    @EventListener
    fun sendErrorNotification(event: ServerExceptionOccurredEvent) {
        slackNotificationApiClient.postMonitoringMessage(
            PostSlackMessageRequest.of(
                SlackMessageTemplateType.ERROR_MESSAGE.generateMessage(
                    event.applicationType.description,
                    event.errorCode.code,
                    event.requestUri,
                    event.exception,
                    event.timeStamp,
                    event.errorCode.message,
                    event.userMetaValue,
                )
            )
        )
    }

    @Async
    @EventListener
    fun sendInfoNotification(event: ApplicationStateChangedEvent) {
        slackNotificationApiClient.postMonitoringMessage(
            PostSlackMessageRequest.of(
                SlackMessageTemplateType.INFO_MESSAGE.generateMessage(
                    event.applicationType.description,
                    event.applicationUid,
                    event.message,
                    event.timeStamp
                )))
    }

}
