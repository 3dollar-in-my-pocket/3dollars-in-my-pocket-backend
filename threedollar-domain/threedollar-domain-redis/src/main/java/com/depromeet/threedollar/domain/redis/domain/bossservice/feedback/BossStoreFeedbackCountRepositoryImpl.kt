package com.depromeet.threedollar.domain.redis.domain.bossservice.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import org.springframework.stereotype.Repository

private const val DEFAULT_COUNT = 0

@Repository
class BossStoreFeedbackCountRepositoryImpl(
    private val bossStoreFeedbackCountRepository: StringRedisRepository<BossStoreFeedbackCountKey, Int>,
) : BossStoreFeedbackCountRepository {

    override fun getTotalCounts(bossStoreId: String): Int {
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

    override fun getTotalCountsMap(bossStoreIds: List<String>): Map<String, Int> {
        val totalCountsDictionary: MutableMap<String, Int> = mutableMapOf()
        bossStoreIds.forEach { bossStoreId ->
            totalCountsDictionary[bossStoreId] = getTotalCounts(bossStoreId)
        }
        return totalCountsDictionary
    }

    override fun increase(bossStoreId: String, feedbackType: BossStoreFeedbackType) {
        val key = BossStoreFeedbackCountKey.of(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
        bossStoreFeedbackCountRepository.incr(key)
    }

    override fun increaseBulk(bossStoreId: String, feedbackTypes: Set<BossStoreFeedbackType>) {
        val keys = feedbackTypes.map { feedbackType ->
            BossStoreFeedbackCountKey.of(
                bossStoreId = bossStoreId,
                feedbackType = feedbackType
            )
        }
        bossStoreFeedbackCountRepository.incrBulk(keys)
    }

    override fun getCount(bossStoreId: String, feedbackType: BossStoreFeedbackType): Int {
        val key = BossStoreFeedbackCountKey.of(
            bossStoreId = bossStoreId,
            feedbackType = feedbackType
        )
        return bossStoreFeedbackCountRepository.get(key) ?: DEFAULT_COUNT
    }

    override fun getAllCountsGroupByFeedbackType(bossStoreId: String): Map<BossStoreFeedbackType, Int> {
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
