package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

enum class BossStoreStatus(
    private val description: String,
) {

    ACTIVE("활성화 중인 가게"),
    DELETED("삭제된 가게"),

}
