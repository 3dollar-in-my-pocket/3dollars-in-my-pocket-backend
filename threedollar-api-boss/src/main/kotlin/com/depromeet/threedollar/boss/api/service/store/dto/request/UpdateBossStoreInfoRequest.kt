package com.depromeet.threedollar.boss.api.service.store.dto.request

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.store.BossStoreAppearanceDay
import com.depromeet.threedollar.document.boss.document.store.BossStoreMenu
import com.depromeet.threedollar.document.common.document.TimeInterval
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import java.time.LocalTime
import javax.validation.constraints.Size

data class UpdateBossStoreInfoRequest(
    @field:Length(max = 30, message = "{store.name.length}")
    val name: String,

    @field:URL(message = "{store.imageUrl.url}")
    val imageUrl: String?,
    val introduction: String?,
    val contactsNumber: String?,

    @field:URL(message = "{store.snsUrl.url}")
    val snsUrl: String?,

    val menus: List<MenuRequest>,

    val appearanceDays: Set<AppearanceDayRequest>,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
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
