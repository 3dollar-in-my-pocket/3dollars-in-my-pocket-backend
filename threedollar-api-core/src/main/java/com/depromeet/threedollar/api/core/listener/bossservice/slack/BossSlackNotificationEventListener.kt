package com.depromeet.threedollar.api.core.listener.bossservice.slack

import com.depromeet.threedollar.common.type.template.SlackMessageTemplateType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationApprovedEvent
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationDeniedEvent
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.NewBossAppliedRegistrationEvent
import com.depromeet.threedollar.infrastructure.external.client.slack.SlackWebhookApiClient
import com.depromeet.threedollar.infrastructure.external.client.slack.dto.request.PostSlackMessageRequest
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class BossSlackNotificationEventListener(
    private val slackNotificationApiClient: SlackWebhookApiClient,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) {

    @Async
    @EventListener
    fun sendRegistrationNotification(event: NewBossAppliedRegistrationEvent) {
        slackNotificationApiClient.postBossManagerMessage(
            PostSlackMessageRequest.of(
                SlackMessageTemplateType.NEW_BOSS_REGISTRATION_MESSAGE.generateMessage(
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
    fun sendBossRegistrationApprovedMessage(event: BossRegistrationApprovedEvent) {
        slackNotificationApiClient.postBossManagerMessage(
            PostSlackMessageRequest.of(SlackMessageTemplateType.BOSS_REGISTRATION_APPROVED_MESSAGE.generateMessage(event.bossRegistrationId))
        )
    }

    @Async
    @EventListener
    fun sendBossRegistrationDeniedMessage(event: BossRegistrationDeniedEvent) {
        slackNotificationApiClient.postBossManagerMessage(
            PostSlackMessageRequest.of(SlackMessageTemplateType.BOSS_REGISTRATION_DENIED_MESSAGE.generateMessage(event.bossRegistrationId))
        )
    }

}
