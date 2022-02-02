package com.depromeet.threedollar.document.boss.document.category

import com.depromeet.threedollar.document.common.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_store_category_v1")
class BossStoreCategory(
    val name: String,
    val sequencePriority: Int
) : BaseDocument()
