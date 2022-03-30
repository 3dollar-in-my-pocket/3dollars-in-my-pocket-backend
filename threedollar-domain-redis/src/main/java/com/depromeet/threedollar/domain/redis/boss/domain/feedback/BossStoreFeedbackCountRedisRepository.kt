package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository

private const val DEFAULT_COUNT = 0

@Repository
class BossStoreFeedbackCountRedisRepository(
    private val bossStoreFeedbackCountRepository: StringRedisRepository<BossStoreFeedbackCountRedisKey, Int>
) {

    fun getTotalCount(bossStoreId: String): Int {
        return bossStoreFeedbackCountRepository.getBulk(
            BossStoreFeedbackType.values().map {
                BossStoreFeedbackCountRedisKey(bossStoreId = bossStoreId, feedbackType = it)
            })
            .filterNotNull()
            .sum()
    }

    fun increase(bossStoreId: String, feedbackType: BossStoreFeedbackType) {
        val key = BossStoreFeedbackCountRedisKey.of(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
        bossStoreFeedbackCountRepository.increase(key)
    }

    fun incrementAll(bossStoreId: String, feedbackTypes: Set<BossStoreFeedbackType>) {
        val keys = feedbackTypes.map {
            BossStoreFeedbackCountRedisKey.of(
                bossStoreId = bossStoreId,
                feedbackType = it
            )
        }
        bossStoreFeedbackCountRepository.increaseBulk(keys)
    }

    fun get(bossStoreId: String, feedbackType: BossStoreFeedbackType): Int {
        val key = BossStoreFeedbackCountRedisKey.of(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
        return bossStoreFeedbackCountRepository.get(key) ?: DEFAULT_COUNT
    }

    fun getAll(bossStoreId: String): Map<BossStoreFeedbackType, Int> {
        val feedbackTypes = BossStoreFeedbackType.values()
        val feedbackCounts: List<Int> = bossStoreFeedbackCountRepository.getBulk(feedbackTypes
            .map { BossStoreFeedbackCountRedisKey.of(bossStoreId, it) })

        val feedbackCountsMap = linkedMapOf<BossStoreFeedbackType, Int>()
        feedbackTypes.forEachIndexed { index, feedbackType ->
            run {
                feedbackCountsMap[feedbackType] = feedbackCounts[index] ?: DEFAULT_COUNT
            }
        }
        return feedbackCountsMap
    }

}
