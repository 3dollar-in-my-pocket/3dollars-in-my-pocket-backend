package com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response

import com.depromeet.threedollar.api.core.common.dto.response.CursorResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import java.time.LocalDate

data class BossStoreFeedbackCursorResponse(
    val cursor: CursorResponse<LocalDate>,
    val contents: List<BossStoreFeedbackGroupingDateResponse>,
) {

    companion object {
        fun of(feedbackGroupingDate: Map<LocalDate, Map<BossStoreFeedbackType, Int>>, nextDate: LocalDate?): BossStoreFeedbackCursorResponse {
            return BossStoreFeedbackCursorResponse(
                cursor = CursorResponse.of(nextDate),
                contents = feedbackGroupingDate.asSequence()
                    .sortedByDescending { (date, _) -> date }
                    .map { (date, feedbackCountsGroupingType) ->
                        BossStoreFeedbackGroupingDateResponse.of(date = date, feedbackCountsGroupingType = feedbackCountsGroupingType)
                    }
                    .toList()
            )
        }
    }

}


data class BossStoreFeedbackGroupingDateResponse(
    val date: LocalDate,
    val feedbacks: List<BossStoreFeedbackCountResponse>,
) {

    companion object {
        fun of(date: LocalDate, feedbackCountsGroupingType: Map<BossStoreFeedbackType, Int>): BossStoreFeedbackGroupingDateResponse {
            return BossStoreFeedbackGroupingDateResponse(
                date = date,
                feedbacks = feedbackCountsGroupingType.map { (feedbackType, count) ->
                    BossStoreFeedbackCountResponse.of(feedbackType = feedbackType, count = count)
                }
            )
        }
    }

}
