package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.springframework.data.geo.Point
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
            location = Point(longitude, latitude)
        )
    }

}
