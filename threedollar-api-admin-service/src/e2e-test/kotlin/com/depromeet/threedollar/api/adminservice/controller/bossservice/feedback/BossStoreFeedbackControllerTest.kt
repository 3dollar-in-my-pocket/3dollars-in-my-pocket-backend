package com.depromeet.threedollar.api.adminservice.controller.bossservice.feedback

import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.adminservice.SetupAdminControllerTest
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType

internal class BossStoreFeedbackControllerTest : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @DisplayName("GET /boss/v1/boss/store/feedback/types")
    @Test
    fun `사장님 가게 피드백의 타입 목록을 조회한다`() {
        // when & then
        mockMvc.get("/v1/boss/store/feedback/types") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", Matchers.hasSize<BossStoreFeedbackTypeResponse>(BossStoreFeedbackType.values().size))

                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.HANDS_ARE_FAST.name) }
                jsonPath("$.data[0].description") { value(BossStoreFeedbackType.HANDS_ARE_FAST.description) }
                jsonPath("$.data[0].emoji") { value(BossStoreFeedbackType.HANDS_ARE_FAST.emoji) }

                jsonPath("$.data[-1].feedbackType") { value(BossStoreFeedbackType.GOT_A_BONUS.name) }
                jsonPath("$.data[-1].description") { value(BossStoreFeedbackType.GOT_A_BONUS.description) }
                jsonPath("$.data[-1].emoji") { value(BossStoreFeedbackType.GOT_A_BONUS.emoji) }
            }
    }

}
