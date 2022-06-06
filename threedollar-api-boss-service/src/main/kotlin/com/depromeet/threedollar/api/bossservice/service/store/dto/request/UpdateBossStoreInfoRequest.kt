package com.depromeet.threedollar.api.bossservice.service.store.dto.request

import java.time.LocalTime
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu

data class UpdateBossStoreInfoRequest(
    @field:Size(max = 30, message = "{store.name.size}")
    val name: String,

    @field:URL(message = "{store.imageUrl.url}")
    @field:Size(max = 2048, message = "{store.imageUrl.size}")
    val imageUrl: String?,

    @field:Size(max = 1024, message = "{store.introduction.size}")
    val introduction: String?,

    val contactsNumber: String?,

    @field:URL(message = "{store.snsUrl.url}")
    @field:Size(max = 2048, message = "{store.snsUrl.size}")
    val snsUrl: String?,

    @field:Size(max = 20, message = "{store.menu.size}")
    val menus: List<MenuRequest>,

    val appearanceDays: Set<AppearanceDayRequest>,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val categoriesIds: Set<String>,
) {

    fun toMenus(): List<BossStoreMenu> {
        return menus.map { menuRequest -> menuRequest.toMenu() }
    }

    fun toAppearanceDays(): Set<BossStoreAppearanceDay> {
        return appearanceDays.asSequence()
            .map { appearanceDayRequest -> appearanceDayRequest.toAppearanceDay() }
            .toSet()
    }

}


data class MenuRequest(
    val name: String,
    val price: Int,
    val imageUrl: String?,
) {

    fun toMenu(): BossStoreMenu {
        return BossStoreMenu.of(
            name = name,
            price = price,
            imageUrl = imageUrl
        )
    }

}


data class AppearanceDayRequest(
    val dayOfTheWeek: DayOfTheWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val locationDescription: String = "",
) {

    fun toAppearanceDay(): BossStoreAppearanceDay {
        return BossStoreAppearanceDay.of(
            dayOfTheWeek = dayOfTheWeek,
            startTime = startTime,
            endTime = endTime,
            locationDescription = locationDescription
        )
    }

}
