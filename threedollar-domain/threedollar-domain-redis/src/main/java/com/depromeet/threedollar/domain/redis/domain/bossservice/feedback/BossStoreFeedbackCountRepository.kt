package com.depromeet.threedollar.domain.redis.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType

interface BossStoreFeedbackCountRepository {

    fun getTotalCounts(bossStoreId: String): Int

    fun increase(bossStoreId: String, feedbackType: BossStoreFeedbackType)

    fun increaseBulk(bossStoreId: String, feedbackTypes: Set<BossStoreFeedbackType>)

    fun getCount(bossStoreId: String, feedbackType: BossStoreFeedbackType): Int

    fun getAllCountsGroupByFeedbackType(bossStoreId: String): Map<BossStoreFeedbackType, Int>

}
