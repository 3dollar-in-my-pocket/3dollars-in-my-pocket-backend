package com.depromeet.threedollar.api.core.service.boss.feedback

import com.depromeet.threedollar.api.core.service.boss.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.response.BossStoreFeedbackCursorResponse
import com.depromeet.threedollar.api.core.service.boss.store.BossStoreCommonServiceUtils
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.feedback.BossStoreFeedbackCountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class BossStoreFeedbackService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository
) {

    @Transactional
    fun addFeedback(bossStoreId: String, request: AddBossStoreFeedbackRequest, userId: Long, date: LocalDate) {
        BossStoreCommonServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        validateNotExistsFeedbackOnDate(storeId = bossStoreId, userId = userId, date = date)
        bossStoreFeedbackRepository.saveAll(request.toDocuments(bossStoreId, userId, date))
        bossStoreFeedbackCountRepository.increaseBulk(bossStoreId, request.feedbackTypes)
    }

    private fun validateNotExistsFeedbackOnDate(storeId: String, userId: Long, date: LocalDate) {
        if (bossStoreFeedbackRepository.existsByStoreIdAndUserIdAndDate(storeId, userId, date)) {
            throw ConflictException("해당 날짜($date)에 유저($userId)는 해당 사장님 가게($storeId)에 이미 피드백을 추가하였습니다", ErrorCode.CONFLICT_BOSS_STORE_FEEDBACK)
        }
    }

    @Transactional(readOnly = true)
    fun getBossStoreFeedbacksCounts(bossStoreId: String): List<BossStoreFeedbackCountResponse> {
        BossStoreCommonServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        val feedbackCountsGroupingByFeedbackType: Map<BossStoreFeedbackType, Int> = bossStoreFeedbackCountRepository.getAllCountsGroupByFeedbackType(bossStoreId)
        return feedbackCountsGroupingByFeedbackType
            .map { BossStoreFeedbackCountResponse.of(it.key, it.value) }
    }

    @Transactional(readOnly = true)
    fun getBossStoreFeedbacksCountsBetweenDate(bossStoreId: String, request: GetBossStoreFeedbacksCountsBetweenDateRequest): BossStoreFeedbackCursorResponse {
        BossStoreCommonServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        val feedbacks = bossStoreFeedbackRepository.findAllByBossStoreIdAndBetween(bossStoreId = bossStoreId, startDate = request.startDate, endDate = request.endDate)

        val feedbacksGroupByDate: Map<LocalDate, Map<BossStoreFeedbackType, Int>> = feedbacks
            .groupBy { it.date }
            .entries
            .associate { it -> it.key to it.value.groupingBy { it.feedbackType }.eachCount() }

        return BossStoreFeedbackCursorResponse.of(
            feedbackGroupingDate = feedbacksGroupByDate,
            nextDate = getNextDate(bossStoreId, feedbacks)
        )
    }

    private fun getNextDate(bossStoreId: String, feedbacks: List<BossStoreFeedback>): LocalDate? {
        if (feedbacks.isEmpty()) {
            return null
        }
        return bossStoreFeedbackRepository.findFirstLessThanDate(bossStoreId = bossStoreId, date = feedbacks.minOf { it.date })?.date
    }

}
