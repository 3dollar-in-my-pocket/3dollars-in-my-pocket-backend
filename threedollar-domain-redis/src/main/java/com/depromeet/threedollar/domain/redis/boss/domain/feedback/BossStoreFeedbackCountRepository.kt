package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType

interface BossStoreFeedbackCountRepository {

    fun increment(bossStoreId: String, feedbackType: BossStoreFeedbackType)

    fun getAll(bossStoreId: String): Map<BossStoreFeedbackType, Int>

    fun getAllCounts(bossStoreId: String): Int

}
