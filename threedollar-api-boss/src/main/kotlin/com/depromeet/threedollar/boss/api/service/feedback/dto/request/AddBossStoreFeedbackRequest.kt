package com.depromeet.threedollar.boss.api.service.feedback.dto.request

import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedback
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackType
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
