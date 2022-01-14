package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.AuditingTimeDocument
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document("boss_store_category_v1")
class BossStoreCategory(
    title: String
) : AuditingTimeDocument() {

    @MongoId
    lateinit var id: String

}
