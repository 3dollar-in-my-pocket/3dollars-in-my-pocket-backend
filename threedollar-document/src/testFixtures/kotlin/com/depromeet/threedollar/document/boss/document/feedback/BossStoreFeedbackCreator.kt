package com.depromeet.threedollar.document.boss.document.feedback

import com.depromeet.threedollar.document.TestFixture
import java.time.LocalDate

@TestFixture
object BossStoreFeedbackCreator {

    fun create(
        storeId: String,
        userId: Long,
        feedbackType: BossStoreFeedbackType,
        date: LocalDate
    ): BossStoreFeedback {
        return BossStoreFeedback(
            storeId = storeId,
            userId = userId,
            feedbackType = feedbackType,
            date = date
        )
    }

}
