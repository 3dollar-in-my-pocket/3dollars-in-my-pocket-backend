package com.depromeet.threedollar.api.boss.service.store.dto.request

import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.store.BossStoreMenu

data class PatchBossStoreInfoRequest(
    @field:Size(max = 30, message = "{store.name.size}")
    val name: String? = null,

    @field:URL(message = "{store.imageUrl.url}")
    @field:Size(max = 2048, message = "{store.imageUrl.size}")
    val imageUrl: String? = null,

    @field:Size(max = 1024, message = "{store.introduction.size}")
    val introduction: String? = null,

    val contactsNumber: String? = null,

    @field:URL(message = "{store.snsUrl.url}")
    @field:Size(max = 2048, message = "{store.snsUrl.size}")
    val snsUrl: String? = null,

    val menus: List<MenuRequest>? = null,

    val appearanceDays: Set<AppearanceDayRequest>? = null,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val categoriesIds: Set<String>? = null,
) {

    fun toMenus(): List<BossStoreMenu>? {
        return menus?.map {
            it.toMenu()
        }
    }

    fun toAppearanceDays(): Set<BossStoreAppearanceDay>? {
        return appearanceDays?.asSequence()
            ?.map { it.toAppearanceDay() }
            ?.toSet()
    }

}
