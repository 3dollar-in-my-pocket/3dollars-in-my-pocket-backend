package com.depromeet.threedollar.redis.boss.domain.store

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import java.time.LocalDateTime

@RedisHash(value = "boss:store:open", timeToLive = 60 * 30)
data class BossStoreOpenInfo(
    @Id
    val bossStoreId: String,
    val openStartDateTime: LocalDateTime
) : Serializable {

    companion object {
        fun of(bossStoreId: String, startDateTime: LocalDateTime): BossStoreOpenInfo {
            return BossStoreOpenInfo(
                bossStoreId = bossStoreId,
                openStartDateTime = startDateTime
            )
        }
    }

}
