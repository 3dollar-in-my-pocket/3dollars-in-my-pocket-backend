package com.depromeet.threedollar.domain.mongo.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.mongo.TestFixture
import java.time.LocalDate

@TestFixture
object BossStoreFeedbackFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: String,
        userId: Long = 300000L,
        feedbackType: BossStoreFeedbackType = BossStoreFeedbackType.BOSS_IS_KIND,
        date: LocalDate = LocalDate.of(2022, 1, 1),
    ): BossStoreFeedback {
        return BossStoreFeedback(
            bossStoreId = storeId,
            userId = userId,
            feedbackType = feedbackType,
            date = date
        )
    }

}
