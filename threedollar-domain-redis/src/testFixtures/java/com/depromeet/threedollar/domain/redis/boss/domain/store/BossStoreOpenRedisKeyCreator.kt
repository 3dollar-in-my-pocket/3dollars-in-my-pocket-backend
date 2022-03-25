package com.depromeet.threedollar.domain.redis.boss.domain.store

import com.depromeet.threedollar.domain.redis.TestFixture

@TestFixture
object BossStoreOpenRedisKeyCreator {

    fun create(
        bossStoreId: String
    ): BossStoreOpenRedisKey {
        return BossStoreOpenRedisKey(
            bossStoreId = bossStoreId
        )
    }

}
