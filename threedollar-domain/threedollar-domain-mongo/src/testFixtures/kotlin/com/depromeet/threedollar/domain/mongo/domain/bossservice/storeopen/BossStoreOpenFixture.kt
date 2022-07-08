package com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen

import java.time.LocalDateTime
import com.depromeet.threedollar.domain.mongo.TestFixture

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
