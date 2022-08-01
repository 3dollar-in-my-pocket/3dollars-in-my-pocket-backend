package com.depromeet.threedollar.domain.redis.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.TestFixture

@TestFixture
object BossStoreFeedbackCountKeyFixture {

    @JvmStatic
    @JvmOverloads
    fun create(
        bossStoreId: String = "boss-store-id",
        feedbackType: BossStoreFeedbackType = BossStoreFeedbackType.BOSS_IS_KIND,
    ): BossStoreFeedbackCountKey {
        return BossStoreFeedbackCountKey(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
    }

}
