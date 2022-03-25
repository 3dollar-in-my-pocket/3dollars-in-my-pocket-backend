package com.depromeet.threedollar.domain.redis.boss.domain.store

import com.depromeet.threedollar.common.utils.JsonUtils
import com.depromeet.threedollar.domain.redis.core.StringRedisKey
import com.depromeet.threedollar.domain.redis.type.REDIS_KEY_DELIMITER
import com.depromeet.threedollar.domain.redis.type.RedisKeyType.BOSS_STORE_OPEN
import java.time.Duration
import java.time.LocalDateTime

data class BossStoreOpenRedisKey(
    private val bossStoreId: String
) : StringRedisKey<LocalDateTime> {

    override fun getKey(): String {
        return BOSS_STORE_OPEN.baseKey + REDIS_KEY_DELIMITER + bossStoreId
    }

    override fun getValue(value: String?): LocalDateTime? {
        return value?.let { JsonUtils.toObject(value, LocalDateTime::class.java) }
    }

    override fun toValue(value: LocalDateTime): String {
        return JsonUtils.toJson(value)
    }

    override fun getTtl(): Duration? {
        return BOSS_STORE_OPEN.ttl
    }

}
