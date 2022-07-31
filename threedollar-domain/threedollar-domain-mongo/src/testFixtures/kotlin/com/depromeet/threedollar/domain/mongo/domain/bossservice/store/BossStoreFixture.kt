package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.domain.mongo.TestFixture

@TestFixture
object BossStoreFixture {

    fun create(
        bossId: String = "boss-id",
        name: String = "사장님 푸드트럭 이름",
        location: BossStoreLocation? = null,
        imageUrl: String = "https://image.png",
        introduction: String = "푸드트럭에 대한 소개",
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
            categoriesIds = categoriesIds,
            location = location
        )
    }

}
