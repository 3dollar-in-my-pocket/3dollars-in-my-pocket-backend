package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.TestFixture
import com.depromeet.threedollar.document.common.document.ContactsNumber
import org.springframework.data.geo.Point

@TestFixture
object BossStoreCreator {

    fun create(
        bossId: String,
        name: String,
        latitude: Double = 34.0,
        longitude: Double = 128.0,
        imageUrl: String = "https://image.png",
        introduction: String = "introduction",
        contactsNumber: ContactsNumber? = null,
        snsUrl: String? = null,
        menus: List<BossStoreMenu> = mutableListOf(),
        appearanceDays: List<BossStoreAppearanceDay> = mutableListOf(),
        categories: List<String> = mutableListOf(),
        status: BossStoreStatus = BossStoreStatus.ACTIVE
    ): BossStore {
        return BossStore(
            bossId = bossId,
            name = name,
            location = Point(longitude, latitude),
            imageUrl = imageUrl,
            introduction = introduction,
            contactsNumber = contactsNumber,
            snsUrl = snsUrl,
            menus = menus,
            appearanceDays = appearanceDays,
            categories = categories,
            status = status
        )
    }

}
