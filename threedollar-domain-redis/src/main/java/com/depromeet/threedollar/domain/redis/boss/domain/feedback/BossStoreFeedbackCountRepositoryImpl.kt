package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.domain.redis.type.RedisKeyType
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class BossStoreFeedbackCountRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, Objects>
): BossStoreFeedbackCountRepository {

    override fun increment(bossStoreId: String, feedbackType: BossStoreFeedbackType) {
        val counter: HashOperations<String, String, Int> = redisTemplate.opsForHash()
        counter.increment(getKey(bossStoreId), feedbackType.name, 1)
    }

    override fun getAll(bossStoreId: String): Map<BossStoreFeedbackType, Int> {
        val feedbackCounters: HashOperations<String, String, Int> = redisTemplate.opsForHash()
        val countsMap: Map<String, Int> = feedbackCounters.entries(getKey(bossStoreId))
        return BossStoreFeedbackType.values()
            .associateWith { countsMap[it.name] ?: 0 }
    }

    override fun getAllCounts(bossStoreId: String): Int {
        val feedbackCounters: HashOperations<String, String, Int> = redisTemplate.opsForHash()
        val countsMap: Map<String, Int> = feedbackCounters.entries(getKey(bossStoreId))
        return countsMap
            .map { it.value }
            .sumOf {it }
    }

    private fun getKey(bossStoreId: String): String {
        return "${RedisKeyType.BOSS_STORE_FEEDBACK_KEY}:${bossStoreId}"
    }

}
