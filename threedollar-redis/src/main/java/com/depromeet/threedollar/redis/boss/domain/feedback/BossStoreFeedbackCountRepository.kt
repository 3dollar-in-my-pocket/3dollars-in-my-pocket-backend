package com.depromeet.threedollar.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.Objects

@Repository
class BossStoreFeedbackCountRepository(
    private val redisTemplate: RedisTemplate<String, Objects>
) {

    fun increment(bossStoreId: String, feedbackType: BossStoreFeedbackType) {
        val counter: HashOperations<String, String, Int> = redisTemplate.opsForHash()
        counter.increment(getKey(bossStoreId), feedbackType.name, 1)
    }

    fun getAll(bossStoreId: String): Map<BossStoreFeedbackType, Int> {
        val feedbackCounters: HashOperations<String, String, Int> = redisTemplate.opsForHash()
        val countsMap: Map<String, Int> = feedbackCounters.entries(getKey(bossStoreId))
        return BossStoreFeedbackType.values()
            .associateWith { countsMap[it.name] ?: 0 }
    }

    private fun getKey(bossStoreId: String): String {
        return "$BOSS_STORE_FEEDBACK_KEY:${bossStoreId}"
    }

    companion object {
        private const val BOSS_STORE_FEEDBACK_KEY = "boss:store:feedback"
    }

}
