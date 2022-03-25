package com.depromeet.threedollar.domain.redis.type

import java.time.Duration

const val REDIS_KEY_DELIMITER = ":"

enum class RedisKeyType(
    val baseKey: String,
    val ttl: Duration?
) {

    BOSS_STORE_OPEN("boss:store:open", Duration.ofMinutes(30)),
    BOSS_STORE_FEEDBACK_KEY("boss:store:feedback", null)

}
