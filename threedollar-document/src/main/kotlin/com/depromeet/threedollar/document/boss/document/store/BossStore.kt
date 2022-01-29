package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.ContactsNumber
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_v1")
class BossStore(
    val bossId: String,
    val name: String,
    val imageUrl: String?,
    val introduction: String?,
    val contactsNumber: ContactsNumber?,
    val snsUrl: String?,
    val menus: List<BossStoreMenu> = mutableListOf(),
    val appearanceDays: List<BossStoreAppearanceDay> = mutableListOf(),
    val categoriesIds: List<String> = mutableListOf(),
    val status: BossStoreStatus = BossStoreStatus.ACTIVE
) : BaseDocument()
