package com.depromeet.threedollar.api.core.service.boss.feedback.dto.response

import com.depromeet.threedollar.common.type.BossStoreFeedbackType

data class BossStoreFeedbackTypeResponse(
    val feedbackType: BossStoreFeedbackType,
    val description: String,
    val emoji: String,
) {

    companion object {
        fun of(feedbackType: BossStoreFeedbackType): BossStoreFeedbackTypeResponse {
            return BossStoreFeedbackTypeResponse(
                feedbackType = feedbackType,
                description = feedbackType.description,
                emoji = feedbackType.emoji
            )
        }
    }

}
