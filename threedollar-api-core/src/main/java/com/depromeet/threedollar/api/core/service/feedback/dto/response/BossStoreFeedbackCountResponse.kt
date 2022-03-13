package com.depromeet.threedollar.api.core.service.feedback.dto.response

import com.depromeet.threedollar.common.type.BossStoreFeedbackType

data class BossStoreFeedbackCountResponse(
    val feedbackType: BossStoreFeedbackType,
    val count: Int
) {

    companion object {
        fun of(feedbackType: BossStoreFeedbackType, count: Int): BossStoreFeedbackCountResponse {
            return BossStoreFeedbackCountResponse(
                feedbackType = feedbackType,
                count = count
            )
        }
    }

}
