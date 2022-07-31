package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCountWithRatioResponse

data class BossStoreWithFeedbacksResponse(
    val store: BossStoreInfoResponse,
    val feedbacks: List<BossStoreFeedbackCountWithRatioResponse>,
) {

    companion object {
        fun of(
            store: BossStoreInfoResponse,
            feedbacks: List<BossStoreFeedbackCountWithRatioResponse>,
        ): BossStoreWithFeedbacksResponse {
            return BossStoreWithFeedbacksResponse(
                store = store,
                feedbacks = feedbacks,
            )
        }
    }

}
