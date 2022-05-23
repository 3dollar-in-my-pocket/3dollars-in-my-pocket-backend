package com.depromeet.threedollar.api.core.service.foodtruck.feedback.dto.request

import java.time.LocalDate
import javax.validation.constraints.Size
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.feedback.BossStoreFeedback

data class AddBossStoreFeedbackRequest(
    @field:Size(min = 1, message = "{feedback.size.min}")
    val feedbackTypes: Set<BossStoreFeedbackType> = setOf(),
) {

    fun toDocuments(storeId: String, userId: Long, date: LocalDate): List<BossStoreFeedback> {
        return feedbackTypes.map {
            BossStoreFeedback.of(
                bossStoreId = storeId,
                userId = userId,
                feedbackType = it,
                date = date
            )
        }
    }

}
