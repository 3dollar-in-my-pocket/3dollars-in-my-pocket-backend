package com.depromeet.threedollar.api.bossservice.service.store.dto.request

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu
import org.hibernate.validator.constraints.URL
import javax.validation.Valid
import javax.validation.constraints.Size

data class PatchBossStoreInfoRequest(
    @field:Size(max = 20, message = "{store.name.size}")
    val name: String? = null,

    @field:URL(message = "{store.imageUrl.url}")
    @field:Size(max = 300, message = "{store.imageUrl.size}")
    val imageUrl: String? = null,

    @field:Size(max = 1024, message = "{store.introduction.size}")
    val introduction: String? = null,

    val contactsNumber: String? = null,

    @field:URL(message = "{store.snsUrl.url}")
    @field:Size(max = 300, message = "{store.snsUrl.size}")
    val snsUrl: String? = null,

    @field:Valid
    @field:Size(max = 20, message = "{store.menu.size}")
    val menus: List<MenuRequest>? = null,

    @field:Valid
    val appearanceDays: Set<AppearanceDayRequest>? = null,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val categoriesIds: Set<String>? = null,
) {

    fun toMenus(): List<BossStoreMenu>? {
        return menus?.let {
            return it.map { menuRequest -> menuRequest.toMenu() }
        }
    }

    fun toAppearanceDays(): Set<BossStoreAppearanceDay>? {
        return appearanceDays?.let {
            return it.asSequence()
                .map { appearanceDayRequest -> appearanceDayRequest.toAppearanceDay() }
                .toSet()
        }
    }

}
