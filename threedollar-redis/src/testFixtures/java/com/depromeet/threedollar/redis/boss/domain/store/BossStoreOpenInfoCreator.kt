package com.depromeet.threedollar.redis.boss.domain.store

import com.depromeet.threedollar.redis.TestFixture
import java.time.LocalDateTime

@TestFixture
object BossStoreOpenInfoCreator {

    @JvmStatic
    fun create(
        bossStoreId: String,
        openStartDateTime: LocalDateTime
    ): BossStoreOpenInfo {
        return BossStoreOpenInfo(bossStoreId, openStartDateTime)
    }

}
