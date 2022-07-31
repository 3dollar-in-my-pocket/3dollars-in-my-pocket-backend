package com.depromeet.threedollar.api.userservice.controller.device

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.userservice.SetupUserControllerTest
import com.depromeet.threedollar.api.userservice.controller.device.dto.request.UpsertUserDeviceRequest
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

internal class UserDeviceControllerTest(
    private val deviceRepository: DeviceRepository,
) : SetupUserControllerTest() {

    @AfterEach
    fun cleanUp() {
        deviceRepository.deleteAll()
    }

    @DisplayName("PATCH /api/v1/device IOS")
    @Test
    fun `유저 계정의 IOS 디바이스 정보를 추가한다`() {
        // given
        val request = UpsertUserDeviceRequest(
            pushPlatformType = PushPlatformType.FCM,
            pushToken = "push-token"
        )

        // when
        mockMvc.put("/v1/device") {
            header(HttpHeaders.AUTHORIZATION, token)
            header(HttpHeaders.USER_AGENT, "1.0.0 (com.macgongmon.-dollar-in-my-pocket; build:1; iOS 15.5.0)")
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
                accountId = user.id.toString(),
                accountType = AccountType.USER_ACCOUNT,
                pushToken = request.pushToken,
                pushPlatformType = request.pushPlatformType,
                appVersion = "1.0.0",
                osPlatformType = OsPlatformType.IPHONE
            )
        })
    }

    @DisplayName("PATCH /api/v1/device AOS")
    @Test
    fun `유저 계정의 AOS 디바이스 정보를 추가한다`() {
        // given
        val request = UpsertUserDeviceRequest(
            pushPlatformType = PushPlatformType.FCM,
            pushToken = "push-token"
        )

        // when
        mockMvc.put("/v1/device") {
            header(HttpHeaders.AUTHORIZATION, token)
            header(HttpHeaders.USER_AGENT, "okhttp/4.9.1")
            header("X-ANDROID-SERVICE-VERSION", "1.0.1")
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
                accountId = user.id.toString(),
                accountType = AccountType.USER_ACCOUNT,
                pushToken = request.pushToken,
                pushPlatformType = request.pushPlatformType,
                appVersion = "1.0.1",
                osPlatformType = OsPlatformType.ANDROID
            )
        })
    }

    @DisplayName("DELETE /api/v1/device")
    @Test
    fun `유저 계정의 디바이스를 삭제한다`() {
        // given
        deviceRepository.save(DeviceFixture.create(
            accountType = AccountType.USER_ACCOUNT,
            accountId = user.id.toString(),
        ))

        // when
        mockMvc.delete("/v1/device") {
            header(HttpHeaders.AUTHORIZATION, token)
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
