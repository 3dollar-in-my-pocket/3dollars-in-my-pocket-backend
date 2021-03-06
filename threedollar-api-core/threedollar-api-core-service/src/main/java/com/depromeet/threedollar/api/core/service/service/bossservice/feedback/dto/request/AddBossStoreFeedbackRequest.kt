package com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback.BossStoreFeedback
import java.time.LocalDate
import javax.validation.constraints.Size

data class AddBossStoreFeedbackRequest(
    @field:Size(min = 1, message = "{feedback.size.min}")
    val feedbackTypes: Set<BossStoreFeedbackType>,
) {

    fun toDocuments(storeId: String, userId: Long, date: LocalDate): List<BossStoreFeedback> {
        return feedbackTypes.map { feedbackType ->
            BossStoreFeedback.of(
                bossStoreId = storeId,
                userId = userId,
                feedbackType = feedbackType,
                date = date
            )
        }
    }

}
