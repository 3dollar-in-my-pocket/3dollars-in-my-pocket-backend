package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen

import com.depromeet.threedollar.domain.mongo.TestFixture
import java.time.LocalDateTime

@TestFixture
object BossStoreOpenFixture {

    @JvmStatic
    fun create(
        bossStoreId: String,
        openStartDateTime: LocalDateTime,
        expiredAt: LocalDateTime,
    ): BossStoreOpen {
        return BossStoreOpen(
            bossStoreId = bossStoreId,
            openStartDateTime = openStartDateTime,
            expiredAt = expiredAt
        )
    }

}
