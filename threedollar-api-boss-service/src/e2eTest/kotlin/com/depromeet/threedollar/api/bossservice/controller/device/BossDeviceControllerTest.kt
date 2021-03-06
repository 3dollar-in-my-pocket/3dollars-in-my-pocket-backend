package com.depromeet.threedollar.api.bossservice.controller.device

import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.bossservice.controller.device.dto.request.UpsertBossDeviceRequest
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.common.type.OsPlatformType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.Device
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceFixture
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.PushPlatformType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.put

internal class BossDeviceControllerTest(
    private val deviceRepository: DeviceRepository,
) : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        deviceRepository.deleteAll()
    }

    @DisplayName("PATCH /boss/v1/device IOS")
    @Test
    fun `사장님 계정의 IOS 디바이스 정보를 추가한다`() {
        // given
        val request = UpsertBossDeviceRequest(
            pushPlatformType = PushPlatformType.FCM,
            pushToken = "push-token"
        )

        // when
        mockMvc.put("/v1/device") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            header(HttpHeaders.USER_AGENT, "1.0.0 (com.macgongmon.-dollar-in-my-pocket-manager-dev; build:7; iOS 15.5.0)")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }

        // then
        val devices = deviceRepository.findAll()
        assertAll({
            assertThat(devices).hasSize(1)
            assertDevice(
                device = devices[0],
                accountId = bossId,
                accountType = AccountType.BOSS_ACCOUNT,
                pushToken = request.pushToken,
                pushPlatformType = request.pushPlatformType,
                appVersion = "1.0.0",
                osPlatformType = OsPlatformType.IPHONE
            )
        })
    }

    @DisplayName("PATCH /boss/v1/device AOS")
    @Test
    fun `사장님 계정의 AOS 디바이스 정보를 추가한다`() {
        // given
        val request = UpsertBossDeviceRequest(
            pushPlatformType = PushPlatformType.FCM,
            pushToken = "push-token"
        )

        // when
        mockMvc.put("/v1/device") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            header(HttpHeaders.USER_AGENT, "okhttp/4.9.1")
            header("X-ANDROID-SERVICE-VERSION", "2.4.8")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }

        // then
        val devices = deviceRepository.findAll()
        assertAll({
            assertThat(devices).hasSize(1)
            assertDevice(
                device = devices[0],
                accountId = bossId,
                accountType = AccountType.BOSS_ACCOUNT,
                pushToken = request.pushToken,
                pushPlatformType = request.pushPlatformType,
                appVersion = "2.4.8",
                osPlatformType = OsPlatformType.ANDROID
            )
        })
    }

    @DisplayName("DELETE /boss/v1/device")
    @Test
    fun `사장님 계정의 디바이스를 삭제한다`() {
        // given
        deviceRepository.save(DeviceFixture.create(
            accountType = AccountType.BOSS_ACCOUNT,
            accountId = bossId,
        ))

        // when
        mockMvc.delete("/v1/device") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }

        // then
        val devices = deviceRepository.findAll()
        assertThat(devices).isEmpty()
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

}
