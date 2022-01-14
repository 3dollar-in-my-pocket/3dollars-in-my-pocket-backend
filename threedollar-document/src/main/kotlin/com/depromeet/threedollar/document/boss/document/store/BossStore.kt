package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.BaseDocument
import com.depromeet.threedollar.document.common.document.Coordinate
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_v1")
class BossStore(
    val bossId: String,
    val name: String,
    val coordinate: Coordinate,
    val imageUrl: String = "",
    val introduction: String = "",
    val openInfo: BossStoreOpenInfo,
    val menus: List<BossStoreMenu> = mutableListOf(),
    val appearanceDays: List<BossStoreAppearanceDay> = mutableListOf(),
    val categories: List<String> = mutableListOf(),
    val status: BossStoreStatus
) : BaseDocument()
