package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber

@Document("boss_store_v1")
class BossStore(
    val bossId: String,
    var name: String,
    var location: BossStoreLocation?,
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

    fun patchInfo(
        name: String?,
        imageUrl: String?,
        introduction: String?,
        contactsNumber: String?,
        snsUrl: String?
    ) {
        name?.let { this.name = it }
        imageUrl?.let { this.imageUrl = it }
        introduction?.let { this.introduction = it }
        contactsNumber?.let { this.contactsNumber = ContactsNumber.of(it) }
        snsUrl?.let { this.snsUrl = it }
    }

    fun updateMenus(menus: List<BossStoreMenu>) {
        this.menus = menus
    }

    fun patchMenus(menus: List<BossStoreMenu>?) {
        menus?.let { updateMenus(it) }
    }

    fun updateAppearanceDays(appearanceDays: Set<BossStoreAppearanceDay>) {
        this.appearanceDays = appearanceDays
    }

    fun patchAppearanceDays(appearanceDays: Set<BossStoreAppearanceDay>?) {
        appearanceDays?.let { updateAppearanceDays(it) }
    }

    fun updateCategoriesIds(categoriesIds: Set<String>) {
        this.categoriesIds = categoriesIds
    }

    fun isNotOwner(bossId: String): Boolean {
        return !isOwner(bossId)
    }

    private fun isOwner(bossId: String): Boolean {
        return this.bossId == bossId
    }

    fun hasChangedLocation(latitude: Double, longitude: Double): Boolean {
        this.location?.let {
            return it.hasChangedLocation(latitude = latitude, longitude = longitude)
        }
        return true
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        this.location = BossStoreLocation.of(latitude = latitude, longitude = longitude)
    }

    companion object {
        fun of(
            bossId: String,
            name: String,
            location: BossStoreLocation? = null,
            imageUrl: String? = null,
            introduction: String? = null,
            contactsNumber: ContactsNumber? = null,
            snsUrl: String? = null,
            menus: List<BossStoreMenu> = listOf(),
            appearanceDays: Set<BossStoreAppearanceDay> = setOf(),
            categoriesIds: Set<String> = setOf()
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
                categoriesIds = categoriesIds,
                location = location
            )
        }
    }

}
