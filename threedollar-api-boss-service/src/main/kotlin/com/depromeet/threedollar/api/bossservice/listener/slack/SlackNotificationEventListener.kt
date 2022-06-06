package com.depromeet.threedollar.api.bossservice.listener.slack

import java.util.stream.Collectors
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import com.depromeet.threedollar.common.model.event.ApplicationStateChangedEvent
import com.depromeet.threedollar.common.model.event.ServerExceptionOccurredEvent
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.NewBossAppliedRegistrationEvent
import com.depromeet.threedollar.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.external.client.slack.dto.request.PostSlackMessageRequest
import com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType
import com.depromeet.threedollar.external.client.slack.type.SlackNotificationMessageType.NEW_BOSS_REGISTRATION_MESSAGE

@Component
class SlackNotificationEventListener(
    private val slackNotificationApiClient: SlackWebhookApiClient,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) {

    @Async
    @EventListener
    fun sendRegistrationNotification(event: NewBossAppliedRegistrationEvent) {
        slackNotificationApiClient.postBossManagerMessage(
            PostSlackMessageRequest.of(
                NEW_BOSS_REGISTRATION_MESSAGE.generateMessage(
                    event.bossRegistration.id,
                    event.bossRegistration.boss.name,
                    event.bossRegistration.boss.socialInfo.socialType,
                    event.bossRegistration.boss.businessNumber.getNumberWithSeparator(),
                    event.bossRegistration.store.name,
                    bossStoreCategoryRepository.findAllCategoriesByIds(event.bossRegistration.store.categoriesIds).stream()
                        .map { bossStoreCategory -> bossStoreCategory.name }
                        .collect(Collectors.joining(", ")),
                    event.bossRegistration.store.contactsNumber.getNumberWithSeparator(),
                    event.bossRegistration.store.certificationPhotoUrl
                )
            )
        )
    }

    @Async
    @EventListener
    fun sendErrorNotification(event: ServerExceptionOccurredEvent) {
        slackNotificationApiClient.postMonitoringMessage(
            PostSlackMessageRequest.of(
                SlackNotificationMessageType.ERROR_MESSAGE.generateMessage(
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
                SlackNotificationMessageType.INFO_MESSAGE.generateMessage(
                    event.applicationType.description,
                    event.applicationUid,
                    event.message,
                    event.timeStamp
                )))
    }

}
