package com.depromeet.threedollar.domain.redis.boss.domain.store

import com.depromeet.threedollar.domain.redis.TestFixture
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
