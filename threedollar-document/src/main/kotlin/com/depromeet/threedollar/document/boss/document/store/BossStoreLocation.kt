package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.LocationValidator
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_location_v1")
class BossStoreLocation(
    val bossStoreId: String,
    var location: Point
) : BaseDocument() {

    init {
        LocationValidator.validate(latitude = location.y, longitude = location.x)
    }

    fun hasChangedLocation(latitude: Double, longitude: Double): Boolean {
        return !hasSameLocation(latitude = latitude, longitude = longitude)
    }

    private fun hasSameLocation(latitude: Double, longitude: Double): Boolean {
        return this.location.x == longitude && location.y == latitude

    }

    fun updateLocation(latitude: Double, longitude: Double) {
        this.location = Point(longitude, latitude)
    }

    companion object {
        fun of(bossStoreId: String, latitude: Double, longitude: Double): BossStoreLocation {
            return BossStoreLocation(
                bossStoreId = bossStoreId,
                location = Point(longitude, latitude)
            )
        }
    }

}
