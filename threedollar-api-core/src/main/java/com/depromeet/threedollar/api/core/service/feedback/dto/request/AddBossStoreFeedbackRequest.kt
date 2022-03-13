package com.depromeet.threedollar.api.core.service.feedback.dto.request

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback
import java.time.LocalDate

data class AddBossStoreFeedbackRequest(
    val feedbackType: BossStoreFeedbackType
) {

    fun toDocument(storeId: String, userId: Long, date: LocalDate): BossStoreFeedback {
        return BossStoreFeedback(
            storeId = storeId,
            userId = userId,
            feedbackType = feedbackType,
            date = date
        )
    }

}
