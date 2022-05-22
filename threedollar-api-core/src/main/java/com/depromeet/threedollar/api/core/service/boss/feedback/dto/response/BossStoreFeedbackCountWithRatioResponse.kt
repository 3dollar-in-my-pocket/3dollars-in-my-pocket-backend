package com.depromeet.threedollar.api.core.service.boss.feedback.dto.response

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.utils.MathUtils

data class BossStoreFeedbackCountWithRatioResponse(
    val feedbackType: BossStoreFeedbackType,
    val count: Int,
    val ratio: Double,
) {

    companion object {
        fun of(feedbackType: BossStoreFeedbackType, count: Int, totalCount: Int): BossStoreFeedbackCountWithRatioResponse {
            return BossStoreFeedbackCountWithRatioResponse(
                feedbackType = feedbackType,
                count = count,
                ratio = MathUtils.round(MathUtils.divide(count, totalCount), 2)
            )
        }
    }

}
