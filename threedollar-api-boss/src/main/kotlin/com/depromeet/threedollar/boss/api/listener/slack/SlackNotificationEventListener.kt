package com.depromeet.threedollar.boss.api.listener.slack

import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.event.registration.NewBossAppliedRegistrationEvent
import com.depromeet.threedollar.domain.common.event.ServerExceptionOccurredEvent
import com.depromeet.threedollar.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest
import com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType
import com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.NEW_BOSS_REGISTRATION_MESSAGE
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class SlackNotificationEventListener(
    private val slackNotificationApiClient: SlackWebhookApiClient,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @Async
    @EventListener
    fun sendRegistrationNotification(event: NewBossAppliedRegistrationEvent) {
        slackNotificationApiClient.postMessage(
            PostSlackMessageRequest.of(
                NEW_BOSS_REGISTRATION_MESSAGE.generateMessage(
                    event.registration.id,
                    event.registration.boss.name,
                    event.registration.boss.socialInfo.socialType,
                    event.registration.boss.businessNumber.getNumberWithSeparator(),
                    event.registration.store.name,
                    bossStoreCategoryRepository.findCategoriesByIds(event.registration.store.categoriesIds).stream().map { it.name }.collect(Collectors.joining(", ")),
                    event.registration.store.contactsNumber.getNumberWithSeparator(),
                    event.registration.store.certificationPhotoUrl
                )
            )
        )
    }

    @Async
    @EventListener
    fun sendErrorNotification(event: ServerExceptionOccurredEvent) {
        slackNotificationApiClient.postMessage(
            PostSlackMessageRequest.of(
                SlackNotificationMessageType.ERROR_MESSAGE.generateMessage(
                    event.applicationType.description,
                    event.errorCode.code,
                    event.exception,
                    event.timeStamp,
                    event.errorCode.message,
                    event.userMetaValue,
                )
            )
        )
    }

}
