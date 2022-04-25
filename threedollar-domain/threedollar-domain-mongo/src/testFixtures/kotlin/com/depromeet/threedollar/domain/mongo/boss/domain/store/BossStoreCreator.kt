package com.depromeet.threedollar.domain.mongo.boss.domain.store

import com.depromeet.threedollar.domain.mongo.TestFixture
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber

@TestFixture
object BossStoreCreator {

    fun create(
        bossId: String,
        name: String,
        imageUrl: String = "https://image.png",
        introduction: String = "introduction",
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
            categoriesIds = categoriesIds
        )
    }

}
