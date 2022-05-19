package com.depromeet.threedollar.api.core.service.boss.store.dto.response

import java.time.LocalDateTime
import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.api.core.service.boss.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.utils.distance.LocationDistanceUtils
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreMenu
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreOpenType
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval

data class BossStoreInfoResponse(
    val bossStoreId: String,
    val name: String,
    val location: LocationResponse?,
    val imageUrl: String?,
    val introduction: String?,
    val contactsNumber: String?,
    val snsUrl: String?,
    val menus: List<BossStoreMenuResponse>,
    val appearanceDays: Set<BossStoreAppearanceDayResponse>,
    val categories: Set<BossStoreCategoryResponse>,
    val openStatus: BossStoreOpenStatusResponse,
    val distance: Int
) : AuditingTimeResponse() {

    companion object {
        fun of(
            bossStore: BossStore,
            categories: List<BossStoreCategory>,
            openStartDateTime: LocalDateTime?,
            deviceLocation: LocationValue = LocationValue.of(0.0, 0.0)
        ): BossStoreInfoResponse {
            val response = BossStoreInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = bossStore.location?.let { LocationResponse.of(it) },
                imageUrl = bossStore.imageUrl,
                introduction = bossStore.introduction,
                contactsNumber = bossStore.contactsNumber?.getNumberWithSeparator(),
                snsUrl = bossStore.snsUrl,
                menus = bossStore.menus.map { BossStoreMenuResponse.of(it) },
                appearanceDays = bossStore.appearanceDays.asSequence().map { BossStoreAppearanceDayResponse.of(it) }.toSet(),
                categories = categories.asSequence().map { BossStoreCategoryResponse.of(it) }.toSet(),
                openStatus = openStartDateTime?.let { BossStoreOpenStatusResponse.of(it) }
                    ?: BossStoreOpenStatusResponse.close(),
                distance = LocationDistanceUtils.getDistance(
                    LocationValue.of(bossStore.location?.latitude ?: 0.0,
                        bossStore.location?.longitude ?: 0.0
                    ), deviceLocation)
            )
            response.setAuditingTimeByDocument(bossStore)
            return response
        }
    }

}


data class BossStoreAroundInfoResponse(
    val bossStoreId: String,
    val name: String,
    val location: LocationResponse?,
    val menus: List<BossStoreMenuResponse>,
    val categories: Set<BossStoreCategoryResponse>,
    val openStatus: BossStoreOpenStatusResponse,
    val totalFeedbacksCounts: Int,
    val distance: Int,
) : AuditingTimeResponse() {

    companion object {
        fun of(
            bossStore: BossStore,
            categories: List<BossStoreCategory>,
            openStartDateTime: LocalDateTime?,
            totalFeedbacksCounts: Int,
            deviceLocation: LocationValue = LocationValue.of(0.0, 0.0),
        ): BossStoreAroundInfoResponse {
            val response = BossStoreAroundInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = bossStore.location?.let { LocationResponse.of(it) },
                menus = bossStore.menus.map { BossStoreMenuResponse.of(it) },
                categories = categories.asSequence().map { BossStoreCategoryResponse.of(it) }.toSet(),
                openStatus = openStartDateTime?.let { BossStoreOpenStatusResponse.of(it) }
                    ?: BossStoreOpenStatusResponse.close(),
                totalFeedbacksCounts = totalFeedbacksCounts,
                distance = LocationDistanceUtils.getDistance(
                    LocationValue.of(bossStore.location?.latitude ?: 0.0,
                        bossStore.location?.longitude ?: 0.0
                    ), deviceLocation)
            )
            response.setAuditingTimeByDocument(bossStore)
            return response
        }
    }

}

data class LocationResponse(
    val latitude: Double,
    val longitude: Double
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


data class BossStoreMenuResponse(
    val name: String,
    val price: Int,
    val imageUrl: String?,
) {

    companion object {
        fun of(menu: BossStoreMenu): BossStoreMenuResponse {
            return BossStoreMenuResponse(
                name = menu.name,
                price = menu.price,
                imageUrl = menu.imageUrl,
            )
        }
    }

}


data class BossStoreAppearanceDayResponse(
    val dayOfTheWeek: DayOfTheWeek,
    val openingHours: TimeInterval,
    val locationDescription: String
) {

    companion object {
        fun of(appearanceDay: BossStoreAppearanceDay): BossStoreAppearanceDayResponse {
            return BossStoreAppearanceDayResponse(
                dayOfTheWeek = appearanceDay.dayOfTheWeek,
                openingHours = appearanceDay.openingHours,
                locationDescription = appearanceDay.locationDescription
            )
        }
    }

}


data class BossStoreOpenStatusResponse(
    val status: BossStoreOpenType,
    val openStartDateTime: LocalDateTime?
) {

    companion object {
        fun of(openStartDateTime: LocalDateTime): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.OPEN,
                openStartDateTime = openStartDateTime
            )
        }

        fun close(): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.CLOSED,
                openStartDateTime = null
            )
        }
    }

}
