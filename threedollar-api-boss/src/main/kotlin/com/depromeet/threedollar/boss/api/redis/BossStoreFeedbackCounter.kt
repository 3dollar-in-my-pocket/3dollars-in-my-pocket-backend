package com.depromeet.threedollar.boss.api.redis

import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackType
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.Objects

@Repository
class BossStoreFeedbackCounter(
    private val redisTemplate: RedisTemplate<String, Objects>
) {

    fun increment(bossStoreId: String, feedbackType: BossStoreFeedbackType) {
        val counter: HashOperations<String, String, Long> = redisTemplate.opsForHash()
        counter.increment(getKey(bossStoreId), feedbackType.name, 1)
    }

    fun getAllCounts(bossStoreId: String): Map<String, Long> {
        val counter: HashOperations<String, String, Long> = redisTemplate.opsForHash()
        return counter.entries(getKey(bossStoreId))
    }

    private fun getKey(bossStoreId: String): String {
        return "boss:store:feedback:${bossStoreId}"
    }

}
