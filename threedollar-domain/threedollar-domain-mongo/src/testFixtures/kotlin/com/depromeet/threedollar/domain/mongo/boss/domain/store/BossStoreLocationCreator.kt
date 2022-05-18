package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreLocationCreator {

    fun create(
        bossStoreId: String,
        latitude: Double,
        longitude: Double
    ): BossStoreLocation {
        return BossStoreLocation(
            bossStoreId = bossStoreId,
            location = BossStoreCoordinate(latitude = latitude, longitude = longitude)
        )
    }

}
