package com.depromeet.threedollar.api.userservice.controller.bossservice.feedback

import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.userservice.SetupUserControllerTest
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

internal class BossStoreFeedbackControllerTest(
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
    private val bossStoreRepository: BossStoreRepository,
) : SetupUserControllerTest() {

    @AfterEach
    fun cleanUp() {
        bossStoreFeedbackRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/full")
    @Test
    fun `전체 기간동안의 특정 사장님의 가게의 피드백 갯수와 총 개수 중 해당 피드백의 비율을 조회합니다`() {
        // given
        val bossStore = BossStoreFixture.create(
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
                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.HANDS_ARE_FAST.name) }
                jsonPath("$.data[0].count") { value(0) }
                jsonPath("$.data[0].ratio") { value(0.0) }

                jsonPath("$.data[1].feedbackType") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.name) }
                jsonPath("$.data[1].count") { value(1) }
                jsonPath("$.data[1].ratio") { value(0.33) }

                jsonPath("$.data[2].feedbackType") { value(BossStoreFeedbackType.HYGIENE_IS_CLEAN.name) }
                jsonPath("$.data[2].count") { value(0) }
                jsonPath("$.data[2].ratio") { value(0.0) }

                jsonPath("$.data[3].feedbackType") { value(BossStoreFeedbackType.BOSS_IS_KIND.name) }
                jsonPath("$.data[3].count") { value(2) }
                jsonPath("$.data[3].ratio") { value(0.67) }

                jsonPath("$.data[4].feedbackType") { value(BossStoreFeedbackType.CAN_PAY_BY_CARD.name) }
                jsonPath("$.data[4].count") { value(0) }
                jsonPath("$.data[4].ratio") { value(0.0) }

                jsonPath("$.data[5].feedbackType") { value(BossStoreFeedbackType.GOOD_VALUE_FOR_MONEY.name) }
                jsonPath("$.data[5].count") { value(0) }
                jsonPath("$.data[5].ratio") { value(0.0) }

                jsonPath("$.data[6].feedbackType") { value(BossStoreFeedbackType.GOOD_TO_EAT_IN_ONE_BITE.name) }
                jsonPath("$.data[6].count") { value(0) }
                jsonPath("$.data[6].ratio") { value(0.0) }

                jsonPath("$.data[7].feedbackType") { value(BossStoreFeedbackType.GOT_A_BONUS.name) }
                jsonPath("$.data[7].count") { value(0) }
                jsonPath("$.data[7].ratio") { value(0.0) }
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
                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.HANDS_ARE_FAST.name) }
                jsonPath("$.data[0].description") { value(BossStoreFeedbackType.HANDS_ARE_FAST.description) }
                jsonPath("$.data[0].emoji") { value(BossStoreFeedbackType.HANDS_ARE_FAST.emoji) }

                jsonPath("$.data[-1].feedbackType") { value(BossStoreFeedbackType.GOT_A_BONUS.name) }
                jsonPath("$.data[-1].description") { value(BossStoreFeedbackType.GOT_A_BONUS.description) }
                jsonPath("$.data[-1].emoji") { value(BossStoreFeedbackType.GOT_A_BONUS.emoji) }
            }
    }

    @DisplayName("POST /api/v1/boss/store/{bossStoreId}/feedback")
    @Test
    fun `사장님 가게에 새로운 피드백을 추가합니다`() {
        // given
        val bossStore = BossStoreFixture.create(
            bossId = "bossId",
            name = "가슴속 3천원"
        )
        bossStoreRepository.save(bossStore)

        val request = AddBossStoreFeedbackRequest(
            feedbackTypes = setOf(BossStoreFeedbackType.BOSS_IS_KIND, BossStoreFeedbackType.CAN_PAY_BY_CARD)
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
