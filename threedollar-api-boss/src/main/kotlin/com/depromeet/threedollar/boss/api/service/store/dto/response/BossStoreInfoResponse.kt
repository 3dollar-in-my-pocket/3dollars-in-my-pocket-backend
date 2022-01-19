package com.depromeet.threedollar.boss.api.service.store.dto.response

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.store.*
import com.depromeet.threedollar.document.common.document.TimeInterval
import org.springframework.data.geo.Point
import java.time.LocalDateTime

data class BossStoreInfoResponse(
    val name: String,
    val location: LocationResponse,
    val imageUrl: String,
    val introduction: String,
    val openInfo: BossStoreOpenInfoResponse,
    val menus: List<BossStoreMenuResponse>,
    val appearanceDays: List<BossStoreAppearanceDayResponse>,
    val categories: List<String>
) {

    companion object {
        fun of(bossStore: BossStore): BossStoreInfoResponse {
            return BossStoreInfoResponse(
                name = bossStore.name,
                location = LocationResponse.of(bossStore.location),
                imageUrl = bossStore.imageUrl,
                introduction = bossStore.introduction,
                openInfo = BossStoreOpenInfoResponse.of(bossStore.openInfo),
                menus = bossStore.menus.map { BossStoreMenuResponse.of(it) },
                appearanceDays = bossStore.appearanceDays.map { BossStoreAppearanceDayResponse.of(it) },
                categories = bossStore.categories // TODO 카테고리로 변환
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


data class BossStoreOpenInfoResponse(
    val openStatus: BossStoreOpenType,
    val firstOpenDateTme: LocalDateTime?
) {

    companion object {
        fun of(bossStoreOpenInfo: BossStoreOpenInfo): BossStoreOpenInfoResponse {
            return BossStoreOpenInfoResponse(bossStoreOpenInfo.openStatus, bossStoreOpenInfo.firstOpenDateTime)
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
