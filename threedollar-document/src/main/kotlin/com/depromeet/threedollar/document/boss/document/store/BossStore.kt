package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.LocationValidator
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_v1")
class BossStore(
    val bossId: String,
    val name: String,
    val location: Point,
    val imageUrl: String = "",
    val introduction: String = "",
    val contactsNumber: ContactsNumber?,
    val snsUrl: String?,
    val menus: List<BossStoreMenu> = mutableListOf(),
    val appearanceDays: List<BossStoreAppearanceDay> = mutableListOf(),
    val categories: List<String> = mutableListOf(),
    val status: BossStoreStatus = BossStoreStatus.ACTIVE
) : BaseDocument() {

    init {
        LocationValidator.validate(latitude = location.y, longitude = location.x)
    }

}
