package com.depromeet.threedollar.api.user.controller.boss.feedback

import com.depromeet.threedollar.api.core.service.boss.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.feedback.BossStoreFeedbackCountRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import java.time.LocalDate

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

    @DisplayName("GET /api/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/full")
    @Test
    fun `전체 기간동안의 특정 사장님의 가게의 피드백 갯수를 조회한다`() {
        // given
        val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND

        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가슴속 3천원"
        )
        bossStoreRepository.save(bossStore)

        bossStoreFeedbackCountRepository.increment(bossStore.id, feedbackType)
        val bossStoreFeedback = BossStoreFeedbackCreator.create(
            storeId = bossStore.id,
            userId = 10000L,
            feedbackType = feedbackType,
            date = LocalDate.of(2022, 1, 1)
        )
        bossStoreFeedbackRepository.save(bossStoreFeedback)

        // when & then
        mockMvc.get("/v1/boss/store/${bossStore.id}/feedbacks/full")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { hasSize<BossStoreFeedbackCountResponse>(1) }
                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.name) }
                jsonPath("$.data[0].count") { value(0) }

                jsonPath("$.data[1].feedbackType") { value(feedbackType.name) }
                jsonPath("$.data[1].count") { value(1) }

                jsonPath("$.data[2].feedbackType") { value(BossStoreFeedbackType.EASY_TO_EAT.name) }
                jsonPath("$.data[2].count") { value(0) }

                jsonPath("$.data[3].feedbackType") { value(BossStoreFeedbackType.PRICE_IS_CHEAP.name) }
                jsonPath("$.data[3].count") { value(0) }

                jsonPath("$.data[4].feedbackType") { value(BossStoreFeedbackType.THERE_ARE_PLACES_TO_EAT_AROUND.name) }
                jsonPath("$.data[4].count") { value(0) }

                jsonPath("$.data[5].feedbackType") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.name) }
                jsonPath("$.data[5].count") { value(0) }
            }
    }

    @DisplayName("GET /api/v1/boss/store/feedback/types")
    @Test
    fun `사장님 가게 피드백의 타입 목록을 조회한다`() {
        // when & then
        mockMvc.get("/v1/boss/store/feedback/types")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { hasSize<BossStoreFeedbackTypeResponse>(BossStoreFeedbackType.values().size) }
                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.name) }
                jsonPath("$.data[0].description") { value(BossStoreFeedbackType.FOOD_IS_DELICIOUS.description) }
                jsonPath("$.data[5].feedbackType") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.name) }
                jsonPath("$.data[5].description") { value(BossStoreFeedbackType.PLATING_IS_BEAUTIFUL.description) }
            }
    }

}
