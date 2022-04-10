package com.depromeet.threedollar.api.core.service.boss.feedback.dto.request

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.BossStoreFeedback
import java.time.LocalDate

data class AddBossStoreFeedbackRequest(
    val feedbackTypes: Set<BossStoreFeedbackType>
) {

    fun toDocuments(storeId: String, userId: Long, date: LocalDate): List<BossStoreFeedback> {
        return feedbackTypes.map {
            BossStoreFeedback(
                bossStoreId = storeId,
                userId = userId,
                feedbackType = it,
                date = date
            )
        }
    }

}
