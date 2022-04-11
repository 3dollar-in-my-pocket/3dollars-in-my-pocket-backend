package com.depromeet.threedollar.api.core.service.boss.store.dto.response

import java.time.LocalDateTime
import org.springframework.data.geo.Point
import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.utils.LocationDistanceUtils
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreAppearanceDay
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
            location: Point?,
            categories: List<BossStoreCategory>,
            openStartDateTime: LocalDateTime?,
            geoCoordinate: CoordinateValue = CoordinateValue.of(0.0, 0.0)
        ): BossStoreInfoResponse {
            val response = BossStoreInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = location?.let { LocationResponse.of(it) },
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
                    CoordinateValue.of(location?.y ?: 0.0, location?.x ?: 0.0),
                    geoCoordinate
                )
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
            location: Point?,
            categories: List<BossStoreCategory>,
            openStartDateTime: LocalDateTime?,
            totalFeedbacksCounts: Int,
            geoCoordinate: CoordinateValue = CoordinateValue.of(0.0, 0.0),
        ): BossStoreAroundInfoResponse {
            val response = BossStoreAroundInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = location?.let { LocationResponse.of(it) },
                menus = bossStore.menus.map { BossStoreMenuResponse.of(it) },
                categories = categories.asSequence().map { BossStoreCategoryResponse.of(it) }.toSet(),
                openStatus = openStartDateTime?.let { BossStoreOpenStatusResponse.of(it) }
                    ?: BossStoreOpenStatusResponse.close(),
                totalFeedbacksCounts = totalFeedbacksCounts,
                distance = LocationDistanceUtils.getDistance(
                    CoordinateValue.of(location?.y ?: 0.0, location?.x ?: 0.0),
                    geoCoordinate
                )
            )
            response.setAuditingTimeByDocument(bossStore)
            return response
        }
    }

}


data class BossStoreCategoryResponse(
    val categoryId: String,
    val name: String
) {

    companion object {
        fun of(bossStoreCategory: BossStoreCategory): BossStoreCategoryResponse {
            return BossStoreCategoryResponse(
                categoryId = bossStoreCategory.id,
                name = bossStoreCategory.name
            )
        }
    }

}


data class LocationResponse(
    val latitude: Double,
    val longitude: Double
) {

    companion object {
        fun of(location: Point): LocationResponse {
            return LocationResponse(
                latitude = location.y,
                longitude = location.x
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
