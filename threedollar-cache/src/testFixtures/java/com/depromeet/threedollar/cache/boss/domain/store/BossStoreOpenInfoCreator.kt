package com.depromeet.threedollar.cache.boss.domain.store

import com.depromeet.threedollar.cache.TestFixture
import com.depromeet.threedollar.cache.redis.boss.store.BossStoreOpenInfo
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
