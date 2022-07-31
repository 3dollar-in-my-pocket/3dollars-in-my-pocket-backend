package com.depromeet.threedollar.api.core.service.service.bossservice.feedback

import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.service.bossservice.store.BossStoreServiceHelper
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedbackRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.feedback.BossStoreFeedbackCountRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BossStoreFeedbackService(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreFeedbackRepository: BossStoreFeedbackRepository,
    private val bossStoreFeedbackCountRepository: BossStoreFeedbackCountRepository,
) {

    fun addFeedback(bossStoreId: String, request: AddBossStoreFeedbackRequest, userId: Long, date: LocalDate) {
        BossStoreServiceHelper.validateExistsBossStore(bossStoreRepository, bossStoreId)
        validateNotExistsFeedbackOnDate(storeId = bossStoreId, userId = userId, date = date)

        bossStoreFeedbackRepository.saveAll(request.toDocuments(bossStoreId, userId, date))
        bossStoreFeedbackCountRepository.increaseBulk(bossStoreId, request.feedbackTypes)
    }

    private fun validateNotExistsFeedbackOnDate(storeId: String, userId: Long, date: LocalDate) {
        if (bossStoreFeedbackRepository.existsByStoreIdAndUserIdAndDate(storeId, userId, date)) {
            throw ConflictException("해당 날짜($date)에 유저($userId)는 해당 사장님 가게($storeId)에 이미 피드백을 추가하였습니다", ErrorCode.E409_DUPLICATE_BOSS_STORE_FEEDBACK)
        }
    }

}
