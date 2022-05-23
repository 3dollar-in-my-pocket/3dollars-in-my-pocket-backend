package com.depromeet.threedollar.domain.mongo.foodtruck.domain.feedback

import java.time.LocalDate
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreFeedbackCreator {

    fun create(
        storeId: String,
        userId: Long,
        feedbackType: BossStoreFeedbackType,
        date: LocalDate,
    ): BossStoreFeedback {
        return BossStoreFeedback(
            bossStoreId = storeId,
            userId = userId,
            feedbackType = feedbackType,
            date = date
        )
    }

}
