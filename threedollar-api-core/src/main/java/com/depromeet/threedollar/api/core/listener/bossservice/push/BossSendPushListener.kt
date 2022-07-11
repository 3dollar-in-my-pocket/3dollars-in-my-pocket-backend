package com.depromeet.threedollar.api.core.listener.bossservice.push

import com.depromeet.threedollar.common.type.template.PushMessageTemplateType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationApprovedEvent
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationDeniedEvent
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType
import com.depromeet.threedollar.infrastructure.sqs.provider.MessageSendProvider
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendSinglePushRequest
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BossSendPushListener(
    private val messageSendProvider: MessageSendProvider,
    private val deviceRepository: DeviceRepository,
) {

    @Async
    @EventListener
    fun sendBossRegistrationApproveMessage(event: BossRegistrationApprovedEvent) {
        val device: Device? = findDeviceNullable(accountId = event.bossRegistrationId)
        device?.let {
            messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, SendSinglePushRequest.of(
                token = device.deviceInfo.pushToken,
                messageTemplateType = PushMessageTemplateType.BOSS_REGISTRATION_APPROVED_MESSAGE
            ))
        }
    }

    @Async
    @EventListener
    fun sendBossRegistrationDenyMessage(event: BossRegistrationDeniedEvent) {
        val device: Device? = findDeviceNullable(accountId = event.bossRegistrationId)
        device?.let {
            messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, SendSinglePushRequest.of(
                token = device.deviceInfo.pushToken,
                messageTemplateType = PushMessageTemplateType.BOSS_REGISTRATION_DENIED_MESSAGE,
                customBodyMessage = event.rejectReasonType.description,
            ))
        }
    }

    private fun findDeviceNullable(accountId: String): Device? {
        return deviceRepository.findDeviceByAccountIdAndType(accountId = accountId, accountType = AccountType.BOSS_ACCOUNT)
    }

}
