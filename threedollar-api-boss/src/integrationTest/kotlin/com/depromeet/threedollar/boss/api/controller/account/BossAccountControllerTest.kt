package com.depromeet.threedollar.boss.api.controller.account

import com.depromeet.threedollar.boss.api.controller.ControllerTestUtils
import com.depromeet.threedollar.boss.api.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

internal class BossAccountControllerTest : ControllerTestUtils() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @DisplayName("GET /boss/v1/boss/account/my-info 200 OK")
    @Test
    fun `사장님이 자신의 계정 정보를 조회한다`() {
        // when & then
        mockMvc.get("/v1/boss/account/my-info") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.data.bossId") { value(bossId) }
                jsonPath("$.data.socialType") { value(BossAccountSocialType.KAKAO.toString()) }
                jsonPath("$.data.name") { value("테스트 계정") }
                jsonPath("$.data.pushSettingsStatus") { value(PushSettingsStatus.OFF.name) }
            }
        }
    }

    @DisplayName("GET /boss/v1/boss/account/my-info 401")
    @Test
    fun `사장님이 자신의 계정 정보를 조회할때 잘못된 토큰이면 401 에러가 발생한다`() {
        // when & then
        mockMvc.get("/v1/boss/account/my-info") {
            header(HttpHeaders.AUTHORIZATION, "Wrong Token")
        }.andDo {
            print()
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @DisplayName("PUT /boss/v1/boss/account/my-info")
    @Test
    fun `사장님의 자신 계정 정보를 수정한다`() {
        // given
        val request = UpdateBossAccountInfoRequest(
            name = "붕어빵",
            pushSettingsStatus = PushSettingsStatus.ON
        )

        // when & then
        mockMvc.put("/v1/boss/account/my-info") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }
    }

}
