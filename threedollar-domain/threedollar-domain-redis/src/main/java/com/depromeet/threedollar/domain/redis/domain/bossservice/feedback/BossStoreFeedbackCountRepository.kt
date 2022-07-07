package com.depromeet.threedollar.domain.redis.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository

private const val DEFAULT_COUNT = 0

@Repository
class BossStoreFeedbackCountRepository(
    private val bossStoreFeedbackCountRepository: StringRedisRepository<BossStoreFeedbackCountKey, Int>,
) {

    fun getTotalCounts(bossStoreId: String): Int {
        val keys = BossStoreFeedbackType.values()
            .map { feedbackType ->
                BossStoreFeedbackCountKey(
                    bossStoreId = bossStoreId,
                    feedbackType = feedbackType
                )
            }
        return bossStoreFeedbackCountRepository.getBulk(keys).asSequence()
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
        val keys = feedbackTypes.map { feedbackType ->
            BossStoreFeedbackCountKey.of(
                bossStoreId = bossStoreId,
                feedbackType = feedbackType
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
        val keys: List<BossStoreFeedbackCountKey> = feedbackTypes.map { feedbackType ->
            BossStoreFeedbackCountKey.of(bossStoreId = bossStoreId, feedbackType = feedbackType)
        }
        val feedbackCounts: List<Int?> = bossStoreFeedbackCountRepository.getBulk(keys)

        val feedbackCountsMap = linkedMapOf<BossStoreFeedbackType, Int>()
        feedbackTypes.forEachIndexed { index, feedbackType ->
            run {
                feedbackCountsMap[feedbackType] = feedbackCounts[index] ?: DEFAULT_COUNT
            }
        }
        return feedbackCountsMap
    }

}
