package com.depromeet.threedollar.api.core.service.bossservice.feedback

import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.response.BossStoreFeedbackCountWithRatioResponse
import com.depromeet.threedollar.api.core.service.bossservice.feedback.dto.response.BossStoreFeedbackCursorResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.BossStoreServiceHelper
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.type.CacheType.CacheKey.BOSS_STORE_FEEDBACKS_TOTAL_COUNTS
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedback
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BossStoreFeedbackService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
) {

    @CacheEvict(cacheNames = [BOSS_STORE_FEEDBACKS_TOTAL_COUNTS], key = "#bossStoreId")
    fun addFeedback(bossStoreId: String, request: AddBossStoreFeedbackRequest, userId: Long, date: LocalDate) {
        BossStoreServiceHelper.validateExistsBossStore(bossStoreRepository, bossStoreId)
        validateNotExistsFeedbackOnDate(storeId = bossStoreId, userId = userId, date = date)

        bossStoreFeedbackRepository.saveAll(request.toDocuments(bossStoreId, userId, date))
        bossStoreFeedbackCountRepository.increaseBulk(bossStoreId, request.feedbackTypes)
    }

    private fun validateNotExistsFeedbackOnDate(storeId: String, userId: Long, date: LocalDate) {
        if (bossStoreFeedbackRepository.existsByStoreIdAndUserIdAndDate(storeId, userId, date)) {
            throw ConflictException("해당 날짜($date)에 유저($userId)는 해당 사장님 가게($storeId)에 이미 피드백을 추가하였습니다", ErrorCode.CONFLICT_BOSS_STORE_FEEDBACK)
        }
    }

    @Cacheable(cacheNames = [BOSS_STORE_FEEDBACKS_TOTAL_COUNTS], key = "#bossStoreId")
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
