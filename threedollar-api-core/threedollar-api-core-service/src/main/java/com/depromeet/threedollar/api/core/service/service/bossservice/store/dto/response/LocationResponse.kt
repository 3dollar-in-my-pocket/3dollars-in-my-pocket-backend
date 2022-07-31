package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreLocation

data class LocationResponse(
    val latitude: Double,
    val longitude: Double,
) {

    companion object {
        fun of(location: BossStoreLocation): LocationResponse {
            return LocationResponse(
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }

}
