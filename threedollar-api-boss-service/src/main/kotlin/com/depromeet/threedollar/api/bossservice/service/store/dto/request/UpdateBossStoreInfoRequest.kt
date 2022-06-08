package com.depromeet.threedollar.api.bossservice.service.store.dto.request

import javax.validation.Valid
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu

data class UpdateBossStoreInfoRequest(
    @field:Size(max = 20, message = "{store.name.size}")
    val name: String,

    @field:URL(message = "{store.imageUrl.url}")
    @field:Size(max = 300, message = "{store.imageUrl.size}")
    val imageUrl: String?,

    @field:Size(max = 1024, message = "{store.introduction.size}")
    val introduction: String?,

    val contactsNumber: String?,

    @field:URL(message = "{store.snsUrl.url}")
    @field:Size(max = 300, message = "{store.snsUrl.size}")
    val snsUrl: String?,

    @field:Valid
    @field:Size(max = 20, message = "{store.menu.size}")
    val menus: List<MenuRequest>,

    @field:Valid
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
