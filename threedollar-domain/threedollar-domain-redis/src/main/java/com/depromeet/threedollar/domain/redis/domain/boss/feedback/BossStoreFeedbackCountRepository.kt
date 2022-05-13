package com.depromeet.threedollar.domain.redis.domain.boss.feedback

import org.springframework.stereotype.Repository
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository

private const val DEFAULT_COUNT = 0

@Repository
class BossStoreFeedbackCountRepository(
    private val bossStoreFeedbackCountRepository: StringRedisRepository<BossStoreFeedbackCountKey, Int>
) {

    fun getTotalCounts(bossStoreId: String): Int {
        return bossStoreFeedbackCountRepository.getBulk(
            BossStoreFeedbackType.values().map {
                BossStoreFeedbackCountKey(bossStoreId = bossStoreId, feedbackType = it)
            })
            .filterNotNull()
            .sum()
    }

    fun increase(bossStoreId: String, feedbackType: BossStoreFeedbackType) {
        val key = BossStoreFeedbackCountKey.of(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
        bossStoreFeedbackCountRepository.incr(key)
    }

    fun increaseBulk(bossStoreId: String, feedbackTypes: Set<BossStoreFeedbackType>) {
        val keys = feedbackTypes.map {
            BossStoreFeedbackCountKey.of(
                bossStoreId = bossStoreId,
                feedbackType = it
            )
        }
        bossStoreFeedbackCountRepository.incrBulk(keys)
    }

    fun getCount(bossStoreId: String, feedbackType: BossStoreFeedbackType): Int {
        val key = BossStoreFeedbackCountKey.of(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
        return bossStoreFeedbackCountRepository.get(key) ?: DEFAULT_COUNT
    }

    fun getAllCountsGroupByFeedbackType(bossStoreId: String): Map<BossStoreFeedbackType, Int> {
        val feedbackTypes = BossStoreFeedbackType.values()
        val feedbackCounts: List<Int?> = bossStoreFeedbackCountRepository.getBulk(feedbackTypes
            .map { BossStoreFeedbackCountKey.of(bossStoreId, it) })

        val feedbackCountsMap = linkedMapOf<BossStoreFeedbackType, Int>()
        feedbackTypes.forEachIndexed { index, feedbackType ->
            run {
                feedbackCountsMap[feedbackType] = feedbackCounts[index] ?: DEFAULT_COUNT
            }
        }
        return feedbackCountsMap
    }

}
