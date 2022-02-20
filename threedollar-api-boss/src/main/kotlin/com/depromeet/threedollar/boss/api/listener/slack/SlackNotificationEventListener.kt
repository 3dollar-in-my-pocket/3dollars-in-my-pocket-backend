package com.depromeet.threedollar.boss.api.listener.slack

import com.depromeet.threedollar.document.boss.event.registration.NewBossAppliedRegistrationEvent
import com.depromeet.threedollar.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest
import com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.NEW_BOSS_REGISTRATION_MESSAGE
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SlackNotificationEventListener(
    private val slackNotificationApiClient: SlackWebhookApiClient
) {

    @Async
    @EventListener
    fun sendRegistrationNotification(event: NewBossAppliedRegistrationEvent) {
        slackNotificationApiClient.postMessage(
            PostSlackMessageRequest.of(
                NEW_BOSS_REGISTRATION_MESSAGE.generateMessage(
                    event.registration.boss.name,
                    event.registration.store.name,
                    event.registration.store.contactsNumber
                )
            )
        )
    }

}
