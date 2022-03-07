package com.depromeet.threedollar.api.boss.service.feedback.dto.response

import com.depromeet.threedollar.api.core.common.dto.CursorResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import java.time.LocalDate

data class BossStoreFeedbackCursorResponse(
    val cursor: CursorResponse<LocalDate>,
    val contents: List<BossStoreFeedbackGroupingDateResponse>
) {

    companion object {
        fun of(feedbackGroupingDate: Map<LocalDate, Map<BossStoreFeedbackType, Int>>, nextDate: LocalDate?): BossStoreFeedbackCursorResponse {
            return BossStoreFeedbackCursorResponse(
                cursor = CursorResponse.of(nextDate),
                contents = feedbackGroupingDate.asSequence()
                    .sortedByDescending { it.key }
                    .map { BossStoreFeedbackGroupingDateResponse.of(it.key, it.value) }
                    .toList()
            )
        }
    }

}


data class BossStoreFeedbackGroupingDateResponse(
    val date: LocalDate,
    val feedbacks: List<BossStoreFeedbackCountResponse>
) {

    companion object {
        fun of(date: LocalDate, feedbackCountsGroupingType: Map<BossStoreFeedbackType, Int>): BossStoreFeedbackGroupingDateResponse {
            return BossStoreFeedbackGroupingDateResponse(
                date = date,
                feedbacks = feedbackCountsGroupingType.map { BossStoreFeedbackCountResponse.of(it.key, it.value) }
            )
        }
    }

}
