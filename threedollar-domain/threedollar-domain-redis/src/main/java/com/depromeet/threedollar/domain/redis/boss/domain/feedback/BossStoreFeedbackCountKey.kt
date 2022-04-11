package com.depromeet.threedollar.domain.redis.boss.domain.feedback

import java.time.Duration
import com.depromeet.threedollar.common.exception.model.InternalServerException
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey

data class BossStoreFeedbackCountKey(
    val bossStoreId: String,
    val feedbackType: BossStoreFeedbackType
) : StringRedisKey<Int> {

    override fun getKey(): String {
        return "boss:store:$bossStoreId:feedback:$feedbackType:count:v1"
    }

    override fun deserializeValue(value: String?): Int? {
        return value?.toIntOrNull()
    }

    override fun serializeValue(value: Int): String {
        return JsonUtils.toJson(value)
            ?: throw InternalServerException("잘못된 Int 타입의 value($value)가 입력되었습니다")
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
