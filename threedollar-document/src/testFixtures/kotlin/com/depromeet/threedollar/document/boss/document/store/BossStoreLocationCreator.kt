package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.TestFixture
import org.springframework.data.geo.Point

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
