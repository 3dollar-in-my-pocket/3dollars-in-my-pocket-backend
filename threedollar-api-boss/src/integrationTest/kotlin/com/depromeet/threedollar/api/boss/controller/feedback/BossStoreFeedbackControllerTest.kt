package com.depromeet.threedollar.api.boss.controller.feedback

import com.depromeet.threedollar.api.boss.controller.ControllerTestUtils
import com.depromeet.threedollar.api.core.service.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.core.service.feedback.dto.response.BossStoreFeedbackGroupingDateResponse
import com.depromeet.threedollar.api.core.service.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.feedback.BossStoreFeedbackCountRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import java.time.LocalDate

internal class BossStoreFeedbackControllerTest(
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
    private val bossStoreRepository: BossStoreRepository,
) : ControllerTestUtils() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreFeedbackRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/full")
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

    @DisplayName("GET /boss/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/specific")
    @Nested
    inner class GetBossStoreFeedbacksCountsBetweenDate {

        @DisplayName("이전 날에 더 피드백이 없는 경우, nextDate에 null이 반환된다")
        @Test
        fun `특정 기간내의 특정 사장님 가게의 피드백 갯수를 조회합니다 이전 날에 더 피드백이 없는 경우`() {
            // given
            val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
            val date = LocalDate.of(2022, 1, 1)

            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "가슴속 3천원"
            )
            bossStoreRepository.save(bossStore)

            val feedback = BossStoreFeedbackCreator.create(
                storeId = bossStore.id,
                userId = 1000000L,
                feedbackType = feedbackType,
                date = date
            )
            bossStoreFeedbackRepository.save(feedback)

            // when & then
            mockMvc.get("/v1/boss/store/${bossStore.id}/feedbacks/specific?startDate=2022-01-01&endDate=2022-01-02")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.cursor.nextCursor") { value(null) }
                    jsonPath("$.data.cursor.hasMore") { value(false) }
                    jsonPath("$.data.contents") { hasSize<BossStoreFeedbackGroupingDateResponse>(1) }
                    jsonPath("$.data.contents[0].date") { value("2022-01-01") }
                    jsonPath("$.data.contents[0].feedbacks") { hasSize<BossStoreFeedbackGroupingDateResponse>(1) }
                    jsonPath("$.data.contents[0].feedbacks[0].feedbackType") { value(feedbackType.name) }
                    jsonPath("$.data.contents[0].feedbacks[0].count") { value(1) }
                }
        }

        @DisplayName("이전 날에 더 피드백이 있는 경우 nextDate에 해당 피드백의 date가 반환된다")
        @Test
        fun `특정 기간내의 특정 사장님 가게의 피드백 갯수를 조회합니다 이전 날에 더 피드백이 있는경우`() {
            // given
            val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
            val date = LocalDate.of(2022, 1, 1)
            val nextDate = LocalDate.of(2021, 12, 31)

            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "가슴속 3천원"
            )
            bossStoreRepository.save(bossStore)

            val feedback = BossStoreFeedbackCreator.create(
                storeId = bossStore.id,
                userId = 1000000L,
                feedbackType = feedbackType,
                date = date
            )
            val feedbackMore = BossStoreFeedbackCreator.create(
                storeId = bossStore.id,
                userId = 1000000L,
                feedbackType = feedbackType,
                date = nextDate
            )
            bossStoreFeedbackRepository.saveAll(listOf(feedback, feedbackMore))

            // when & then
            mockMvc.get("/v1/boss/store/${bossStore.id}/feedbacks/specific?startDate=2022-01-01&endDate=2022-01-02")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.cursor.nextCursor") { value("2021-12-31") }
                    jsonPath("$.data.cursor.hasMore") { value(true) }
                    jsonPath("$.data.contents") { hasSize<BossStoreFeedbackGroupingDateResponse>(1) }
                    jsonPath("$.data.contents[0].date") { value("2022-01-01") }
                    jsonPath("$.data.contents[0].feedbacks") { hasSize<BossStoreFeedbackGroupingDateResponse>(1) }
                    jsonPath("$.data.contents[0].feedbacks[0].feedbackType") { value(feedbackType.name) }
                    jsonPath("$.data.contents[0].feedbacks[0].count") { value(1) }
                }
        }

    }

    @DisplayName("GET /boss/v1/boss/store/feedback/types")
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
