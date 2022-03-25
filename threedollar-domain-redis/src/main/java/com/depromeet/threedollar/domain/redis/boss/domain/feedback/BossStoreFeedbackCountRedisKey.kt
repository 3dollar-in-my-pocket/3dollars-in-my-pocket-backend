package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey
import com.depromeet.threedollar.domain.redis.type.REDIS_KEY_DELIMITER
import com.depromeet.threedollar.domain.redis.type.RedisKeyType.BOSS_STORE_FEEDBACK_KEY
import java.time.Duration

data class BossStoreFeedbackCountRedisKey(
    val bossStoreId: String,
    val feedbackType: BossStoreFeedbackType
) : StringRedisKey<Int> {

    override fun getKey(): String {
        return BOSS_STORE_FEEDBACK_KEY.baseKey + REDIS_KEY_DELIMITER + bossStoreId + REDIS_KEY_DELIMITER + feedbackType.name
    }

    override fun getValue(value: String?): Int? {
        return value?.toIntOrNull()
    }

    override fun toValue(value: Int): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return BOSS_STORE_FEEDBACK_KEY.ttl
    }

    companion object {
        fun of(bossStoreId: String, feedbackType: BossStoreFeedbackType): BossStoreFeedbackCountRedisKey {
            return BossStoreFeedbackCountRedisKey(
                bossStoreId = bossStoreId,
                feedbackType = feedbackType
            )
        }
    }

}
