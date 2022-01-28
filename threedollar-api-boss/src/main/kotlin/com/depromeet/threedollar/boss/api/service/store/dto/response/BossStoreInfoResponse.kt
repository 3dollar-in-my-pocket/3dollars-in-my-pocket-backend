package com.depromeet.threedollar.boss.api.service.store.dto.response

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import com.depromeet.threedollar.document.boss.document.store.*
import com.depromeet.threedollar.document.common.document.TimeInterval
import com.depromeet.threedollar.redis.boss.domain.store.BossStoreOpenInfo
import org.springframework.data.geo.Point
import java.time.LocalDateTime

data class BossStoreInfoResponse(
    val name: String,
    val location: LocationResponse,
    val imageUrl: String,
    val introduction: String,
    val menus: List<BossStoreMenuResponse>,
    val appearanceDays: List<BossStoreAppearanceDayResponse>,
    val categories: List<BossStoreCategoryResponse>,
    val openStatus: BossStoreOpenStatusResponse
) {

    companion object {
        fun of(bossStore: BossStore, categories: List<BossStoreCategory>, bossStoreOpenInfo: BossStoreOpenInfo?): BossStoreInfoResponse {
            return BossStoreInfoResponse(
                name = bossStore.name,
                location = LocationResponse.of(bossStore.location),
                imageUrl = bossStore.imageUrl,
                introduction = bossStore.introduction,
                menus = bossStore.menus.map { BossStoreMenuResponse.of(it) },
                appearanceDays = bossStore.appearanceDays.map { BossStoreAppearanceDayResponse.of(it) },
                categories = categories.map { BossStoreCategoryResponse.of(it) },
                openStatus = bossStoreOpenInfo?.let { BossStoreOpenStatusResponse.of(it) }
                    ?: BossStoreOpenStatusResponse.close()
            )
        }
    }

}

data class BossStoreCategoryResponse(
    val title: String
) {

    companion object {
        fun of(bossStoreCategory: BossStoreCategory): BossStoreCategoryResponse {
            return BossStoreCategoryResponse(
                title = bossStoreCategory.title
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
    val menuId: String,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val tag: String
) {

    companion object {
        fun of(menu: BossStoreMenu): BossStoreMenuResponse {
            return BossStoreMenuResponse(
                menuId = menu.menuId,
                name = menu.name,
                price = menu.price,
                imageUrl = menu.imageUrl,
                tag = menu.tag
            )
        }
    }

}


data class BossStoreAppearanceDayResponse(
    val day: DayOfTheWeek,
    val openTime: TimeInterval,
    val locationDescription: String
) {

    companion object {
        fun of(appearanceDay: BossStoreAppearanceDay): BossStoreAppearanceDayResponse {
            return BossStoreAppearanceDayResponse(
                day = appearanceDay.day,
                openTime = appearanceDay.openTime,
                locationDescription = appearanceDay.locationDescription
            )
        }
    }

}


data class BossStoreOpenStatusResponse(
    val status: BossStoreOpenType,
    val startDateTime: LocalDateTime?
) {

    companion object {
        fun of(bossStoreOpenInfo: BossStoreOpenInfo): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.OPEN,
                startDateTime = bossStoreOpenInfo.startDateTime
            )
        }

        fun close(): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.CLOSED,
                startDateTime = null
            )
        }
    }

}
