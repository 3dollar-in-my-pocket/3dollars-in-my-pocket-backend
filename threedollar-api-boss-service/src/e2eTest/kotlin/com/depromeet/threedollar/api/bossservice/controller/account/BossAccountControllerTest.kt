package com.depromeet.threedollar.api.bossservice.controller.account

import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.bossservice.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceFixture
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

internal class BossAccountControllerTest(
    private val deviceRepository: DeviceRepository,
) : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @DisplayName("GET /boss/v1/boss/account/me")
    @Nested
    inner class GetMyBossAccountInfoApiTest {

        @Test
        fun `사장님이 자신의 계정 정보를 조회한다`() {
            // when & then
            mockMvc.get("/v1/boss/account/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.bossId") { value(bossId) }
                jsonPath("$.data.socialType") { value(BossAccountSocialType.KAKAO.toString()) }
                jsonPath("$.data.name") { value("테스트 계정") }
                jsonPath("$.data.isSetupNotification") { value(false) }
            }
        }

        @Test
        fun `사장님이 자신의 계정 정보를 조회시 디바이스가 있으면 알림 설정이 되어있다고 표시된다`() {
            // given
            deviceRepository.save(DeviceFixture.create(accountId = bossId, accountType = AccountType.BOSS_ACCOUNT, pushToken = "pushToken"))

            // when & then
            mockMvc.get("/v1/boss/account/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.bossId") { value(bossId) }
                jsonPath("$.data.socialType") { value(BossAccountSocialType.KAKAO.toString()) }
                jsonPath("$.data.name") { value("테스트 계정") }
                jsonPath("$.data.isSetupNotification") { value(true) }
            }
        }

    }

    @DisplayName("PUT /boss/v1/boss/account/me")
    @Test
    fun `사장님의 자신 계정 정보를 수정한다`() {
        // given
        val request = UpdateBossAccountInfoRequest(
            name = "붕어빵",
        )

        // when & then
        mockMvc.put("/v1/boss/account/me") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            jsonPath("$.data") { value(ApiResponse.OK.data) }
        }
    }

}
