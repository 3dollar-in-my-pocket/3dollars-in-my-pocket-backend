package com.depromeet.threedollar.domain.redis.boss.domain.store

import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey
import java.time.Duration
import java.time.LocalDateTime

data class BossStoreOpenTimeKey(
    private val bossStoreId: String
) : StringRedisKey<LocalDateTime> {

    override fun getKey(): String {
        return "boss:store:$bossStoreId:open:time:v1"
    }

    override fun deserializeValue(value: String?): LocalDateTime? {
        return value?.let { JsonUtils.toObject(value, LocalDateTime::class.java) }
    }

    override fun serializeValue(value: LocalDateTime): String {
        return JsonUtils.toJson(value)
            ?: throw IllegalArgumentException("잘못된 LocalDateTime 형태의 value($value)가 입력되었습니다")
    }

    override fun getTtl(): Duration? {
        return Duration.ofMinutes(30)
    }

    companion object {
        fun of(bossStoreId: String): BossStoreOpenTimeKey {
            return BossStoreOpenTimeKey(
                bossStoreId = bossStoreId
            )
        }
    }

}
