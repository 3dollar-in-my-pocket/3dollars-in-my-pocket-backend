package com.depromeet.threedollar.api.core.service.commonservice.device

import com.depromeet.threedollar.api.core.IntegrationTest
import com.depromeet.threedollar.api.core.service.commonservice.device.dto.request.UpsertDeviceRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.UserMetaValue
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceFixture
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.PushPlatformType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class DeviceServiceTest(
    private val deviceService: DeviceService,
    private val deviceRepository: DeviceRepository,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        deviceRepository.deleteAll()
    }

    @Test
    fun `계정에 디바이스 정보가 없는 경우 해당 디바이스 정보를 추가한다`() {
        // given
        val accountId = "accountId"
        val accountType = AccountType.BOSS_ACCOUNT
        val pushToken = "pushToken"
        val pushPlatformType = PushPlatformType.FCM

        val request = UpsertDeviceRequest(
            accountId = accountId,
            accountType = accountType,
            pushToken = pushToken,
            pushPlatformType = pushPlatformType,
            userMetaValue = userMetaValue,
        )

        // when
        deviceService.upsertDevice(request)

        // then
        val devices = deviceRepository.findAll()
        assertAll({
            assertThat(devices).hasSize(1)
            assertDevice(
                device = devices[0],
                accountId = accountId,
                accountType = accountType,
                pushPlatformType = pushPlatformType,
                osPlatformType = userMetaValue.osPlatform,
                appVersion = userMetaValue.appVersion,
                pushToken = pushToken
            )
        })
    }

    @Test
    fun `계정에 같은 디바이스 정보가 있는 경우 디바이스가 추가되지 않는다`() {
        // given
        val accountId = "accountId"
        val accountType = AccountType.BOSS_ACCOUNT
        val pushToken = "pushToken"
        val pushPlatformType = PushPlatformType.FCM

        deviceRepository.save(DeviceFixture.create(
            accountId = accountId,
            accountType = accountType,
            pushPlatformType = pushPlatformType,
            osPlatformType = userMetaValue.osPlatform,
            pushToken = pushToken,
            appVersion = userMetaValue.appVersion
        ))

        val request = UpsertDeviceRequest(
            accountId = accountId,
            accountType = accountType,
            pushToken = pushToken,
            pushPlatformType = pushPlatformType,
            userMetaValue = userMetaValue,
        )

        // when
        deviceService.upsertDevice(request)

        // then
        val devices = deviceRepository.findAll()
        assertAll({
            assertThat(devices).hasSize(1)
            assertDevice(
                device = devices[0],
                accountId = accountId,
                accountType = accountType,
                pushPlatformType = pushPlatformType,
                osPlatformType = userMetaValue.osPlatform,
                appVersion = userMetaValue.appVersion,
                pushToken = pushToken
            )
        })
    }

    @Test
    fun `해당 계정에 다른 토큰이 유입되면 해당 토큰으로 디바이스 정보가 수정된다`() {
        // given
        val accountId = "accountId"
        val accountType = AccountType.BOSS_ACCOUNT
        val pushToken = "pushToken"
        val pushPlatformType = PushPlatformType.FCM

        deviceRepository.save(DeviceFixture.create(
            accountId = accountId,
            accountType = accountType,
            pushPlatformType = pushPlatformType,
            osPlatformType = userMetaValue.osPlatform,
            pushToken = "anotherPushToken",
            appVersion = userMetaValue.appVersion,
        ))

        val request = UpsertDeviceRequest(
            accountId = accountId,
            accountType = accountType,
            pushToken = pushToken,
            pushPlatformType = pushPlatformType,
            userMetaValue = userMetaValue,
        )

        // when
        deviceService.upsertDevice(request)

        // then
        val devices = deviceRepository.findAll()
        assertAll({
            assertThat(devices).hasSize(1)
            assertDevice(
                device = devices[0],
                accountId = accountId,
                accountType = accountType,
                pushPlatformType = pushPlatformType,
                osPlatformType = userMetaValue.osPlatform,
                appVersion = userMetaValue.appVersion,
                pushToken = pushToken
            )
        })
    }

    @Test
    fun `해당 계정에 다른 디바이스 정보가 유입되면 해당 디바이스 정보로 수정된다`() {
        // given
        val accountId = "accountId"
        val accountType = AccountType.BOSS_ACCOUNT
        val pushToken = "pushToken"
        val pushPlatformType = PushPlatformType.FCM

        val request = UpsertDeviceRequest(
            accountId = accountId,
            accountType = accountType,
            pushToken = pushToken,
            pushPlatformType = pushPlatformType,
            userMetaValue = userMetaValue,
        )

        deviceRepository.save(Device.of(
            accountId = accountId,
            accountType = accountType,
            pushPlatformType = pushPlatformType,
            osPlatformType = userMetaValue.osPlatform,
            pushToken = pushToken,
            appVersion = userMetaValue.appVersion,
        ))

        // when
        deviceService.upsertDevice(request)

        // then
        val devices = deviceRepository.findAll()
        assertAll({
            assertThat(devices).hasSize(1)
            assertDevice(
                device = devices[0],
                accountId = accountId,
                accountType = accountType,
                pushPlatformType = pushPlatformType,
                osPlatformType = userMetaValue.osPlatform,
                appVersion = userMetaValue.appVersion,
                pushToken = pushToken
            )
        })
    }

    @Test
    fun `해당 계정에 등록된 디바이스를 삭제한다`() {
        // given
        val accountId = "accountId"
        val accountType = AccountType.BOSS_ACCOUNT
        val pushToken = "pushToken"
        val pushPlatformType = PushPlatformType.FCM

        deviceRepository.save(Device.of(
            accountId = accountId,
            accountType = accountType,
            pushPlatformType = pushPlatformType,
            osPlatformType = userMetaValue.osPlatform,
            pushToken = pushToken,
            appVersion = userMetaValue.appVersion,
        ))

        // when
        deviceService.deleteDevice(accountId = accountId, accountType = accountType)

        // then
        val devices = deviceRepository.findAll()
        assertThat(devices).isEmpty()
    }

    @Test
    fun `해당 계정에 등록된 디바이스를 삭제할때 등록된 디바이스가 없는 경우 NotFound 에러가 발생한다`() {
        // when & then
        assertThatThrownBy { deviceService.deleteDevice(accountId = "NotFound AccountId", accountType = AccountType.USER_ACCOUNT) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun assertDevice(
        device: Device,
        accountId: String,
        accountType: AccountType,
        pushPlatformType: PushPlatformType,
        osPlatformType: OsPlatformType,
        pushToken: String,
        appVersion: String?,
    ) {
        assertAll({
            assertThat(device.accountId).isEqualTo(accountId)
            assertThat(device.accountType).isEqualTo(accountType)
            assertThat(device.deviceInfo.pushPlatformType).isEqualTo(pushPlatformType)
            assertThat(device.deviceInfo.osPlatformType).isEqualTo(osPlatformType)
            assertThat(device.deviceInfo.appVersion).isEqualTo(appVersion)
            assertThat(device.deviceInfo.pushToken).isEqualTo(pushToken)
        })
    }

    companion object {
        private val userMetaValue = UserMetaValue.of(
            osPlatform = OsPlatformType.IPHONE,
            applicationType = ApplicationType.BOSS_API,
            appVersion = "1.0.0",
            userAgent = "userAgent",
            clientIp = "127.0.0.1",
            traceId = "traceId"
        )
    }

}
