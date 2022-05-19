package com.depromeet.threedollar.domain.redis.domain.boss.store

import java.time.Duration
import java.time.LocalDateTime
import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey

data class BossStoreOpenTimeKey(
    private val bossStoreId: String,
) : StringRedisKey<LocalDateTime> {

    override fun getKey(): String {
        return "boss:v1:store:$bossStoreId:open:time"
    }

    override fun deserializeValue(value: String?): LocalDateTime? {
        return value?.let { JsonUtils.toObject(value, LocalDateTime::class.java) }
    }

    override fun serializeValue(value: LocalDateTime): String {
        return JsonUtils.toJson(value)
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
