package com.depromeet.threedollar.api.user.controller.boss.feedback

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository

internal class BossStoreFeedbackControllerTest(
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
    private val bossStoreRepository: BossStoreRepository,
) : SetupUserControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreFeedbackRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/full")
    @Test
    fun `전체 기간동안의 특정 사장님의 가게의 피드백 갯수와 총 개수 중 해당 피드백의 비율을 조회합니다`() {
        // given
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원"
        )
        bossStoreRepository.save(bossStore)

        bossStoreFeedbackCountRepository.increase(bossStore.id, BossStoreFeedbackType.BOSS_IS_KIND)
        bossStoreFeedbackCountRepository.increase(bossStore.id, BossStoreFeedbackType.BOSS_IS_KIND)
        bossStoreFeedbackCountRepository.increase(bossStore.id, BossStoreFeedbackType.FOOD_IS_DELICIOUS)

        // when & then
        mockMvc.get("/v1/boss/store/${bossStore.id}/feedbacks/full")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", hasSize<BossStoreFeedbackCountResponse>(BossStoreFeedbackType.values().size))

                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.name) }
                jsonPath("$.data[0].count") { value(1) }
                jsonPath("$.data[0].ratio") { value(0.33) }

                jsonPath("$.data[1].feedbackType") { value(BossStoreFeedbackType.BOSS_IS_KIND.name) }
                jsonPath("$.data[1].count") { value(2) }
                jsonPath("$.data[1].ratio") { value(0.67) }

                jsonPath("$.data[2].feedbackType") { value(BossStoreFeedbackType.EASY_TO_EAT.name) }
                jsonPath("$.data[2].count") { value(0) }
                jsonPath("$.data[2].ratio") { value(0.0) }

                jsonPath("$.data[3].feedbackType") { value(BossStoreFeedbackType.PRICE_IS_CHEAP.name) }
                jsonPath("$.data[3].count") { value(0) }
                jsonPath("$.data[3].ratio") { value(0.0) }

                jsonPath("$.data[4].feedbackType") { value(BossStoreFeedbackType.THERE_ARE_PLACES_TO_EAT_AROUND.name) }
                jsonPath("$.data[4].count") { value(0) }
                jsonPath("$.data[4].ratio") { value(0.0) }

                jsonPath("$.data[5].feedbackType") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.name) }
                jsonPath("$.data[5].count") { value(0) }
                jsonPath("$.data[5].ratio") { value(0.0) }
            }
    }

    @DisplayName("GET /api/v1/boss/store/feedback/types")
    @Test
    fun `사장님 가게 피드백 종류를 조회합니다`() {
        // when & then
        mockMvc.get("/v1/boss/store/feedback/types")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", hasSize<BossStoreFeedbackTypeResponse>(BossStoreFeedbackType.values().size))
                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.name) }
                jsonPath("$.data[0].description") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.description) }
                jsonPath("$.data[5].feedbackType") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.name) }
                jsonPath("$.data[5].description") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.description) }
            }
    }

    @DisplayName("POST /api/v1/boss/store/{bossStoreId}/feedback")
    @Test
    fun `사장님 가게에 새로운 피드백을 추가합니다`() {
        // given
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원"
        )
        bossStoreRepository.save(bossStore)

        val request = AddBossStoreFeedbackRequest(
            feedbackTypes = setOf(BossStoreFeedbackType.BOSS_IS_KIND, BossStoreFeedbackType.THERE_ARE_PLACES_TO_EAT_AROUND)
        )

        mockMvc.post("/v1/boss/store/${bossStore.id}/feedback") {
            header(HttpHeaders.AUTHORIZATION, token)
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

}
