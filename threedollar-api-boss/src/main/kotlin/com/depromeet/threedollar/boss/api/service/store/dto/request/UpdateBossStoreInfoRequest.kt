package com.depromeet.threedollar.boss.api.service.store.dto.request

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.store.BossStoreAppearanceDay
import com.depromeet.threedollar.document.boss.document.store.BossStoreMenu
import com.depromeet.threedollar.document.common.document.TimeInterval
import java.time.LocalTime

data class UpdateBossStoreInfoRequest(
    val name: String,
    val imageUrl: String?,
    val introduction: String?,
    val contactsNumber: String?,
    val snsUrl: String?,
    val menus: List<MenuRequest>,
    val appearanceDays: Set<AppearanceDayRequest>,
    val categoriesIds: Set<String>
) {

    fun toMenus(): List<BossStoreMenu> {
        return menus.map {
            it.toMenu()
        }
    }

    fun toAppearanceDays(): Set<BossStoreAppearanceDay> {
        return appearanceDays.map {
            it.toAppearanceDay()
        }.toSet()
    }

}


data class MenuRequest(
    val name: String,
    val price: Int,
    val imageUrl: String?,
    val groupName: String
) {

    fun toMenu(): BossStoreMenu {
        return BossStoreMenu(
            name = name,
            price = price,
            imageUrl = imageUrl,
            groupName = groupName
        )
    }

}


data class AppearanceDayRequest(
    val dayOfTheWeek: DayOfTheWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val locationDescription: String = ""
) {

    fun toAppearanceDay(): BossStoreAppearanceDay {
        return BossStoreAppearanceDay(
            dayOfTheWeek = dayOfTheWeek,
            openingHours = TimeInterval(startTime, endTime),
            locationDescription = locationDescription
        )
    }

}
