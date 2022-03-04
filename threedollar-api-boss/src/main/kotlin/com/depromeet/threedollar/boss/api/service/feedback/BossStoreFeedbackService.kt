package com.depromeet.threedollar.boss.api.service.feedback

import com.depromeet.threedollar.redis.boss.domain.feedback.BossStoreFeedbackCountRepository
import com.depromeet.threedollar.boss.api.service.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.boss.api.service.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.boss.api.service.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.boss.api.service.feedback.dto.response.BossStoreFeedbackCursorResponse
import com.depromeet.threedollar.boss.api.service.store.BossStoreServiceUtils
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedback
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
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        validateNotExistsFeedbackOnDate(storeId = bossStoreId, userId = userId, feedbackType = request.feedbackType, date = date)
        bossStoreFeedbackRepository.save(request.toDocument(bossStoreId, userId, date))
        bossStoreFeedbackCountRepository.increment(bossStoreId, request.feedbackType)
    }

    private fun validateNotExistsFeedbackOnDate(storeId: String, userId: Long, feedbackType: BossStoreFeedbackType, date: LocalDate) {
        if (bossStoreFeedbackRepository.existsByStoreIdAndUserIdAndFeedbackTypeAndDate(storeId, userId, feedbackType, date)) {
            throw ConflictException("해당 날짜($date)에 유저($userId)는 해당 가게($storeId)에 이미 해당 피드백(${feedbackType})을 추가하였습니다", ErrorCode.CONFLICT_BOSS_STORE_FEEDBACK)
        }
    }

    @Transactional(readOnly = true)
    fun getBossStoreFeedbacksCounts(bossStoreId: String): List<BossStoreFeedbackCountResponse> {
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        return bossStoreFeedbackCountRepository.getAll(bossStoreId)
            .map { BossStoreFeedbackCountResponse.of(it.key, it.value) }
            .toList()
    }

    @Transactional(readOnly = true)
    fun getBossStoreFeedbacksCountsBetweenDate(bossStoreId: String, request: GetBossStoreFeedbacksCountsBetweenDateRequest): BossStoreFeedbackCursorResponse {
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        val feedbacksBetweenDate = bossStoreFeedbackRepository.findAllByBossStoreIdAndBetween(bossStoreId = bossStoreId, startDate = request.startDate, endDate = request.endDate)

        val feedbacksGroupingDate: Map<LocalDate, Map<BossStoreFeedbackType, Int>> = feedbacksBetweenDate
            .groupBy { it.date }
            .entries
            .associate { it -> it.key to it.value.groupingBy { it.feedbackType }.eachCount() }

        return BossStoreFeedbackCursorResponse.of(
            feedbackGroupingDate = feedbacksGroupingDate,
            nextDate = getNextDate(bossStoreId, feedbacksBetweenDate)
        )
    }

    private fun getNextDate(bossStoreId: String, feedbacks: List<BossStoreFeedback>): LocalDate? {
        if (feedbacks.isEmpty()) {
            return null
        }
        return bossStoreFeedbackRepository.findFirstLessThanDate(bossStoreId = bossStoreId, date = feedbacks.minOf { it.date })?.date
    }

}
