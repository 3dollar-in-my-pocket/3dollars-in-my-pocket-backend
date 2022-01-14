package com.depromeet.threedollar.document.boss.document.store

import org.springframework.data.mongodb.core.mapping.MongoId

class BossStoreMenu(
    val name: String,
    val price: Int,
    val menuCategory: String
) {

    @MongoId
    lateinit var id: String

}
