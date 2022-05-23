package com.depromeet.threedollar.api.admin.controller.foodtruck.feedback

import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.core.service.foodtruck.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType

internal class BossStoreFeedbackControllerTest : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @DisplayName("GET /boss/v1/boss/store/feedback/types")
    @Test
    fun `푸드트럭 피드백의 타입 목록을 조회한다`() {
        // when & then
        mockMvc.get("/v1/boss/store/feedback/types") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", Matchers.hasSize<BossStoreFeedbackTypeResponse>(BossStoreFeedbackType.values().size))

                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.name) }
                jsonPath("$.data[0].description") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.description) }
                jsonPath("$.data[0].emoji") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.emoji) }

                jsonPath("$.data[5].feedbackType") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.name) }
                jsonPath("$.data[5].description") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.description) }
                jsonPath("$.data[5].emoji") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.emoji) }
            }
    }

}
