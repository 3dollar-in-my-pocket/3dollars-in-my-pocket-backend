package com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.TestFixture
import java.time.LocalDate

@TestFixture
object BossStoreFeedbackFixture {

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
