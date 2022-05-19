package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import com.depromeet.threedollar.domain.mongo.common.domain.LocationValidator

@Document("boss_store_location_v1")
class BossStoreLocation(
    val bossStoreId: String,
    var location: BossStoreCoordinate
) : BaseDocument() {

    fun hasChangedLocation(latitude: Double, longitude: Double): Boolean {
        return !this.location.hasSameLocation(latitude = latitude, longitude = longitude)
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        this.location = BossStoreCoordinate(latitude = latitude, longitude = longitude)
    }

    companion object {
        fun of(
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

}


/**
 * (longitude, latitude) 순서로 저장되어야 합니다.
 */
data class BossStoreCoordinate(
    val longitude: Double,
    val latitude: Double
) {

    init {
        LocationValidator.validate(latitude = latitude, longitude = longitude)
    }

    fun hasSameLocation(latitude: Double, longitude: Double): Boolean {
        return this.latitude == latitude && this.longitude == longitude
    }

}
