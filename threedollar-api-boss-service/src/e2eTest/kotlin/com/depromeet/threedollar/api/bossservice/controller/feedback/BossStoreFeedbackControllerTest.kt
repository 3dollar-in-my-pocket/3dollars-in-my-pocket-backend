package com.depromeet.threedollar.api.bossservice.controller.feedback

import com.depromeet.threedollar.api.bossservice.SetupBossStoreControllerTest
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackGroupingDateResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
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
) : SetupBossStoreControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreFeedbackRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/full")
    @Test
    fun `전체 기간동안의 특정 사장님의 가게의 피드백 갯수와 총 개수 중 해당 피드백의 비율을 조회합니다`() {
        // given
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

    @DisplayName("GET /boss/v1/boss/store/{{BOSS_STORE_ID}/feedbacks/specific")
    @Nested
    inner class GetBossStoreFeedbacksCountsBetweenDateTest {

        @DisplayName("이전 날에 더 피드백이 없는 경우, nextDate에 null이 반환된다")
        @Test
        fun `특정 기간내의 특정 사장님 가게의 피드백 갯수를 조회합니다 이전 날에 더 피드백이 없는 경우`() {
            // given
            val feedbackType = BossStoreFeedbackType.BOSS_IS_KIND
            val date = LocalDate.of(2022, 1, 1)

            val feedback = BossStoreFeedbackFixture.create(
                storeId = bossStore.id,
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

                    jsonPath("$.data.contents", hasSize<BossStoreFeedbackGroupingDateResponse>(1))
                    jsonPath("$.data.contents[0].date") { value("2022-01-01") }
                    jsonPath("$.data.contents[0].feedbacks", hasSize<BossStoreFeedbackGroupingDateResponse>(1))
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

            val feedback = BossStoreFeedbackFixture.create(
                storeId = bossStore.id,
                userId = 1000000L,
                feedbackType = feedbackType,
                date = date
            )
            val feedbackMore = BossStoreFeedbackFixture.create(
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

                    jsonPath("$.data.contents", hasSize<BossStoreFeedbackGroupingDateResponse>(1))
                    jsonPath("$.data.contents[0].date") { value("2022-01-01") }
                    jsonPath("$.data.contents[0].feedbacks", hasSize<BossStoreFeedbackGroupingDateResponse>(1))
                    jsonPath("$.data.contents[0].feedbacks[0].feedbackType") { value(feedbackType.name) }
                    jsonPath("$.data.contents[0].feedbacks[0].count") { value(1) }
                }
        }

        @Test
        fun `해당 기간 이전의 피드백이 더 있는 경우 cursor가 다음 피드백의 날짜를 가리킨다`() {
            // given
            val userId = 100000L

            val feedback1 = BossStoreFeedbackFixture.create(
                storeId = bossStore.id,
                userId = userId,
                feedbackType = BossStoreFeedbackType.BOSS_IS_KIND,
                date = LocalDate.of(2022, 1, 1)
            )
            val feedback2 = BossStoreFeedbackFixture.create(
                storeId = bossStore.id,
                userId = userId,
                feedbackType = BossStoreFeedbackType.FOOD_IS_DELICIOUS,
                date = LocalDate.of(2021, 12, 25)
            )
            bossStoreFeedbackRepository.saveAll(listOf(feedback1, feedback2))

            // when & then
            mockMvc.get("/v1/boss/store/${bossStore.id}/feedbacks/specific?startDate=2022-01-02&endDate=2022-01-03")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data.cursor.nextCursor") { value("2022-01-01") }
                    jsonPath("$.data.cursor.hasMore") { value(true) }

                    jsonPath("$.data.contents", hasSize<BossStoreFeedbackGroupingDateResponse>(0))
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
                jsonPath("$.data", hasSize<BossStoreFeedbackTypeResponse>(BossStoreFeedbackType.values().size))

                jsonPath("$.data[0].feedbackType") { value(BossStoreFeedbackType.HANDS_ARE_FAST.name) }
                jsonPath("$.data[0].description") { value(BossStoreFeedbackType.HANDS_ARE_FAST.description) }
                jsonPath("$.data[0].emoji") { value(BossStoreFeedbackType.HANDS_ARE_FAST.emoji) }

                jsonPath("$.data[-1].feedbackType") { value(BossStoreFeedbackType.GOT_A_BONUS.name) }
                jsonPath("$.data[-1].description") { value(BossStoreFeedbackType.GOT_A_BONUS.description) }
                jsonPath("$.data[-1].emoji") { value(BossStoreFeedbackType.GOT_A_BONUS.emoji) }
            }
    }

}
