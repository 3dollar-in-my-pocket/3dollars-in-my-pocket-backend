package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
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
        var categoriesIds: Set<String> = setOf()
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

    companion object {
        fun of(
                bossId: String,
                name: String,
                imageUrl: String? = null,
                introduction: String? = null,
                contactsNumber: ContactsNumber? = null,
                snsUrl: String? = null,
                menus: List<BossStoreMenu> = listOf(),
                appearanceDays: Set<BossStoreAppearanceDay> = setOf(),
                categoriesIds: Set<String> = setOf(),
        ): BossStore {
            return BossStore(
                bossId = bossId,
                name = name,
                imageUrl = imageUrl,
                introduction = introduction,
                contactsNumber = contactsNumber,
                snsUrl = snsUrl,
                menus = menus,
                appearanceDays = appearanceDays,
                categoriesIds = categoriesIds
            )
        }
    }

}
