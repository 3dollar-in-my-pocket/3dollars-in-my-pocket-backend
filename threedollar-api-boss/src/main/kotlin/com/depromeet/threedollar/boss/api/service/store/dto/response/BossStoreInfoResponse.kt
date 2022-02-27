package com.depromeet.threedollar.boss.api.service.store.dto.response

import com.depromeet.threedollar.boss.api.controller.dto.response.BaseTimeResponse
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import com.depromeet.threedollar.document.boss.document.store.*
import com.depromeet.threedollar.document.common.document.TimeInterval
import com.depromeet.threedollar.redis.boss.domain.store.BossStoreOpenInfo
import org.springframework.data.geo.Point
import java.time.LocalDateTime

data class BossStoreInfoResponse(
    val bossStoreId: String,
    val name: String,
    val location: LocationResponse?,
    val imageUrl: String?,
    val introduction: String?,
    val menus: List<BossStoreMenuResponse>,
    val appearanceDays: Set<BossStoreAppearanceDayResponse>,
    val categories: Set<BossStoreCategoryResponse>,
    val openStatus: BossStoreOpenStatusResponse
) : BaseTimeResponse() {

    companion object {
        fun of(
            bossStore: BossStore,
            location: Point?,
            categories: List<BossStoreCategory>,
            bossStoreOpenInfo: BossStoreOpenInfo?
        ): BossStoreInfoResponse {
            val response = BossStoreInfoResponse(
                bossStoreId = bossStore.id,
                name = bossStore.name,
                location = location?.let { LocationResponse.of(it) },
                imageUrl = bossStore.imageUrl,
                introduction = bossStore.introduction,
                menus = bossStore.menus.map { BossStoreMenuResponse.of(it) },
                appearanceDays = bossStore.appearanceDays.map { BossStoreAppearanceDayResponse.of(it) }.toSet(),
                categories = categories.map { BossStoreCategoryResponse.of(it) }.toSet(),
                openStatus = bossStoreOpenInfo?.let { BossStoreOpenStatusResponse.of(it) }
                    ?: BossStoreOpenStatusResponse.close()
            )
            response.setBaseTime(bossStore)
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
    val groupName: String
) {

    companion object {
        fun of(menu: BossStoreMenu): BossStoreMenuResponse {
            return BossStoreMenuResponse(
                name = menu.name,
                price = menu.price,
                imageUrl = menu.imageUrl,
                groupName = menu.groupName
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
        fun of(bossStoreOpenInfo: BossStoreOpenInfo): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.OPEN,
                openStartDateTime = bossStoreOpenInfo.openStartDateTime
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
