package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.ContactsNumber
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_v1")
class BossStore(
    val bossId: String,
    var name: String,
    var imageUrl: String?,
    var introduction: String?,
    var contactsNumber: ContactsNumber?,
    var snsUrl: String?,
    var menus: List<BossStoreMenu> = listOf(),
    var appearanceDays: Set<BossStoreAppearanceDay> = setOf(),
    var categoriesIds: Set<String> = setOf(),
    val status: BossStoreStatus = BossStoreStatus.ACTIVE
) : BaseDocument() {

    fun updateInfo(
        name: String,
        imageUrl: String?,
        introduction: String?,
        contactsNumber: String?,
        snsUrl: String?
    ) {
        this.name = name
        this.imageUrl = imageUrl
        this.introduction = introduction
        this.contactsNumber = contactsNumber?.let { ContactsNumber.of(it) }
        this.snsUrl = snsUrl
    }

    fun updateMenus(menus: List<BossStoreMenu>) {
        this.menus = menus
    }

    fun updateAppearanceDays(appearanceDays: Set<BossStoreAppearanceDay>) {
        this.appearanceDays = appearanceDays
    }

    fun updateCategoriesIds(categoriesIds: Set<String>) {
        this.categoriesIds = categoriesIds
    }

}
