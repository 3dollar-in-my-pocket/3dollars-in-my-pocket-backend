package com.depromeet.threedollar.domain.redis.domain.boss.feedback

import java.time.Duration
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey

data class BossStoreFeedbackCountKey(
    val bossStoreId: String,
    val feedbackType: BossStoreFeedbackType
) : StringRedisKey<Int> {

    override fun getKey(): String {
        return "boss:v1:store:$bossStoreId:feedback:$feedbackType:count"
    }

    override fun deserializeValue(value: String?): Int? {
        return value?.toIntOrNull()
    }

    override fun serializeValue(value: Int): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return null
    }

    companion object {
        fun of(bossStoreId: String, feedbackType: BossStoreFeedbackType): BossStoreFeedbackCountKey {
            return BossStoreFeedbackCountKey(
                bossStoreId = bossStoreId,
                feedbackType = feedbackType
            )
        }
    }

}
