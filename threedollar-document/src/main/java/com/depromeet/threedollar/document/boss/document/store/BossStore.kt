package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.AuditingTimeDocument
import com.depromeet.threedollar.document.common.document.Coordinate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.LocalDateTime

@Document("boss_store_v1")
class BossStore(
    val bossId: String,
    val name: String,
    val coordinate: Coordinate,
    val introduction: String,
    val lastUpdateDateTime: LocalDateTime,
    val isOpen: BossStoreOpenType,
    val menus: List<BossStoreMenu> = mutableListOf(),
    val appearanceDays: List<BossStoreAppearanceDay> = mutableListOf(),
    val categories: List<String>
) : AuditingTimeDocument() {

    @MongoId
    lateinit var id: String

}




