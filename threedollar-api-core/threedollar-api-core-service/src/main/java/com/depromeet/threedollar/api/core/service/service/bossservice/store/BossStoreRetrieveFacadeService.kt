package com.depromeet.threedollar.api.core.service.service.bossservice.store

import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response.BossStoreWithFeedbacksResponse
import com.depromeet.threedollar.common.model.LocationValue
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossStoreRetrieveFacadeService(
    private val bossStoreRetrieveService: BossStoreRetrieveService,
    private val bossStoreFeedbackService: BossStoreFeedbackService,
) {

    @Transactional(readOnly = true)
    fun retrieveBossStoreWithFeedback(
        bossStoreId: String,
        deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
    ): BossStoreWithFeedbacksResponse {
        val store = bossStoreRetrieveService.getBossStore(storeId = bossStoreId, deviceLocation = deviceLocation)
        val feedbacks = bossStoreFeedbackService.getBossStoreFeedbacksCounts(bossStoreId = bossStoreId)
        return BossStoreWithFeedbacksResponse.of(
            store = store,
            feedbacks = feedbacks,
        )
    }

}
