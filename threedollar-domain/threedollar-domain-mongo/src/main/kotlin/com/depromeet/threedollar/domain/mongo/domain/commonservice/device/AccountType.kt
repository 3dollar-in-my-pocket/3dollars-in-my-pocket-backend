package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

enum class AccountType(
    private val description: String,
) {

    BOSS_ACCOUNT("사장님 계정"),
    USER_ACCOUNT("유저 계정"),

}
