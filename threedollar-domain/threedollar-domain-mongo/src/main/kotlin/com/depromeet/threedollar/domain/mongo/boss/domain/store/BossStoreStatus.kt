package com.depromeet.threedollar.domain.mongo.boss.domain.store

enum class BossStoreStatus(
    private val description: String,
) {

    ACTIVE("활성화 중인 가게"),
    DELETED("삭제된 가게"),

}
