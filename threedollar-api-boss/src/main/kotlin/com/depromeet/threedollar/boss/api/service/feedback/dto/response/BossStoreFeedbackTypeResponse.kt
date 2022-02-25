package com.depromeet.threedollar.boss.api.service.feedback.dto.response

import com.depromeet.threedollar.common.type.BossStoreFeedbackType

data class BossStoreFeedbackTypeResponse(
    val feedbackType: BossStoreFeedbackType,
    val description: String
) {

    companion object {
        fun of(bossStoreFeedbackType: BossStoreFeedbackType): BossStoreFeedbackTypeResponse {
            return BossStoreFeedbackTypeResponse(
                feedbackType = bossStoreFeedbackType,
                description = bossStoreFeedbackType.description
            )
        }
    }

}
