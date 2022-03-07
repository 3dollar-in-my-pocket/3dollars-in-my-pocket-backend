package com.depromeet.threedollar.domain.mongo.boss.domain.store

data class BossStoreMenu(
    val name: String,
    val price: Int,
    val imageUrl: String?,
    val groupName: String
)
