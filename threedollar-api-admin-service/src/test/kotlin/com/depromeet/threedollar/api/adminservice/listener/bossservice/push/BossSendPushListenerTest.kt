package com.depromeet.threedollar.api.adminservice.listener.bossservice.push

import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRejectReasonType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceFixture
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationApprovedEvent
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationDeniedEvent
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType
import com.depromeet.threedollar.infrastructure.sqs.provider.MessageSendProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BossSendPushListenerTest {

    private val deviceRepository: DeviceRepository = mockk()
    private val messageSendProvider: MessageSendProvider = mockk()

    private val bossSendPushListener: BossSendPushListener = BossSendPushListener(messageSendProvider, deviceRepository)

    @BeforeEach
    fun setupTest() {
        every { messageSendProvider.sendToTopic(any(), any()) } returns Unit
    }

    @DisplayName("사장님 가입 신청 승인 푸시 알림 리스너")
    @Nested
    inner class SendBossRegistrationApproveMessageTest {

        @Test
        fun `해당하는 계정의 디바이스 정보가 있는 경우 사장님 가입 신청 푸시 알림이 전송된다`() {
            // given
            every { deviceRepository.findDeviceByAccountIdAndType(any(), any()) } returns DeviceFixture.create(
                accountId = "accountId",
                accountType = AccountType.BOSS_ACCOUNT,
                pushToken = "pushToken"
            )

            // when
            bossSendPushListener.sendBossRegistrationApproveMessage(BossRegistrationApprovedEvent(bossRegistrationId = "registrationId"))

            // then
            verify(exactly = 1) { messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, any()) }
        }

        @Test
        fun `해당하는 계정의 디바이스 정보가 없는 경우 푸시 알림이 전송되지 않는다`() {
            // given
            every { deviceRepository.findDeviceByAccountIdAndType(any(), any()) } returns null

            // when
            bossSendPushListener.sendBossRegistrationApproveMessage(BossRegistrationApprovedEvent(bossRegistrationId = "notRegisterDeviceAccountId"))

            // then
            verify(exactly = 0) { messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, any()) }
        }

    }

    @DisplayName("사장님 가입 신청 반려 푸시 알림 리스너")
    @Nested
    inner class SendBossRegistrationDenyMessageTest {

        @Test
        fun `해당하는 계정의 디바이스 정보가 있는 경우 사장님 가입 신청 푸시 알림이 전송된다`() {
            // given
            every { deviceRepository.findDeviceByAccountIdAndType(any(), any()) } returns DeviceFixture.create(
                accountId = "accountId",
                accountType = AccountType.BOSS_ACCOUNT,
                pushToken = "pushToken"
            )

            val event = BossRegistrationDeniedEvent(bossRegistrationId = "registrationId", rejectReasonType = BossRegistrationRejectReasonType.INVALID_BUSINESS_NUMBER)

            // when
            bossSendPushListener.sendBossRegistrationDenyMessage(event)

            // then
            verify(exactly = 1) { messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, any()) }
        }

        @Test
        fun `해당하는 계정의 디바이스 정보가 없는 경우 푸시 알림이 전송되지 않는다`() {
            // given
            every { deviceRepository.findDeviceByAccountIdAndType(any(), any()) } returns null

            val event = BossRegistrationDeniedEvent(bossRegistrationId = "notRegisterDeviceAccountId", rejectReasonType = BossRegistrationRejectReasonType.INVALID_BUSINESS_NUMBER)

            // when
            bossSendPushListener.sendBossRegistrationDenyMessage(event)

            // then
            verify(exactly = 0) { messageSendProvider.sendToTopic(TopicType.BOSS_SINGLE_APP_PUSH, any()) }
        }

    }

}
