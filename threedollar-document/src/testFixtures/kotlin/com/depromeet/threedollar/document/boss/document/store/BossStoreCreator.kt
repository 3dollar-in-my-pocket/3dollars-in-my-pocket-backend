package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.TestFixture
import com.depromeet.threedollar.document.common.document.ContactsNumber

@TestFixture
object BossStoreCreator {

    fun create(
        bossId: String,
        name: String,
        imageUrl: String = "https://image.png",
        introduction: String = "introduction",
        contactsNumber: ContactsNumber? = null,
        snsUrl: String? = null,
        menus: List<BossStoreMenu> = mutableListOf(),
        appearanceDays: List<BossStoreAppearanceDay> = mutableListOf(),
        categoriesIds: List<String> = mutableListOf(),
        status: BossStoreStatus = BossStoreStatus.ACTIVE
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
            status = status
        )
    }

}
