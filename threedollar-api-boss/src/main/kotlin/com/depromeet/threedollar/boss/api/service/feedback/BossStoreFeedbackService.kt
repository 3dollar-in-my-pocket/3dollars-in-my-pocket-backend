package com.depromeet.threedollar.boss.api.service.feedback

import com.depromeet.threedollar.boss.api.redis.BossStoreFeedbackCounter
import com.depromeet.threedollar.boss.api.service.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.boss.api.service.store.BossStoreServiceUtils
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class BossStoreFeedbackService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCounter: BossStoreFeedbackCounter
) {

    @Transactional
    fun addFeedback(bossStoreId: String, request: AddBossStoreFeedbackRequest, userId: Long, date: LocalDate) {
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        validateNotExistsFeedbackToday(storeId = bossStoreId, userId = userId, date = date)
        bossStoreFeedbackRepository.save(request.toDocument(bossStoreId, userId, date))
        bossStoreFeedbackCounter.increment(bossStoreId, request.feedbackType)
    }

    private fun validateNotExistsFeedbackToday(storeId: String, userId: Long, date: LocalDate) {
        if (bossStoreFeedbackRepository.existsByStoreIdAndUserIdAndDate(storeId, userId, date)) {
            throw ConflictException("해당 날짜($date)에 유저($userId)는 해당 가게($storeId)에 이미 피드백을 추가하였습니다", ErrorCode.CONFLICT)
        }
    }

    @Transactional(readOnly = true)
    fun retrieveBossStoreFeedbacks(bossStoreId: String): Map<String, Long> {
        BossStoreServiceUtils.validateExistsBossStore(bossStoreRepository, bossStoreId)
        val counts = bossStoreFeedbackCounter.getAllCounts(bossStoreId) as MutableMap<String, Long>
        for (feedbackType in BossStoreFeedbackType.values()) {
            if (counts[feedbackType.name] == null) {
                counts[feedbackType.name] = 0
            }
        }
        return counts
    }

}
