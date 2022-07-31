package com.depromeet.threedollar.api.core.service.service.bossservice.feedback

import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCountWithRatioResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCursorResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.store.BossStoreServiceHelper
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedback
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BossStoreFeedbackRetrieveService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
) {

    fun getBossStoreFeedbacksCounts(bossStoreId: String): List<BossStoreFeedbackCountWithRatioResponse> {
        BossStoreServiceHelper.validateExistsBossStore(bossStoreRepository, bossStoreId)

        val feedbackCountsGroupingByFeedbackType: Map<BossStoreFeedbackType, Int> = bossStoreFeedbackCountRepository.getAllCountsGroupByFeedbackType(bossStoreId)
        val totalCount = feedbackCountsGroupingByFeedbackType.values.sum()
        return feedbackCountsGroupingByFeedbackType.map { (feedbackType, count) ->
            BossStoreFeedbackCountWithRatioResponse.of(feedbackType = feedbackType, count = count, totalCount = totalCount)
        }
    }

    fun getBossStoreFeedbacksCountsBetweenDate(bossStoreId: String, request: GetBossStoreFeedbacksCountsBetweenDateRequest): BossStoreFeedbackCursorResponse {
        BossStoreServiceHelper.validateExistsBossStore(bossStoreRepository, bossStoreId)
        val feedbacks = bossStoreFeedbackRepository.findAllByBossStoreIdAndBetween(bossStoreId = bossStoreId, startDate = request.startDate, endDate = request.endDate)

        val feedbacksGroupByDate: Map<LocalDate, Map<BossStoreFeedbackType, Int>> = feedbacks
            .groupBy { feedback -> feedback.date }
            .entries
            .associate { (date, feedback) -> date to feedback.groupingBy { it.feedbackType }.eachCount() }

        return BossStoreFeedbackCursorResponse.of(
            feedbackGroupingDate = feedbacksGroupByDate,
            nextDate = getNextDate(bossStoreId = bossStoreId, oldestDateInCursor = request.startDate)
        )
    }

    private fun getNextDate(bossStoreId: String, oldestDateInCursor: LocalDate): LocalDate? {
        val bossStoreFeedback: BossStoreFeedback? = bossStoreFeedbackRepository.findLastLessThanDate(bossStoreId = bossStoreId, date = oldestDateInCursor)
        return bossStoreFeedback?.date
    }

}
