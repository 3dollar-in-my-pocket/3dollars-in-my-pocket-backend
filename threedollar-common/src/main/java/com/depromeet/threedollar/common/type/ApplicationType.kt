package com.depromeet.threedollar.common.type

enum class ApplicationType(
    val description: String
) {

    USER_API("유저 API 서버"),
    BOSS_API("사장님 API 서버"),
    ADMIN_API("관리자 서버"),
    BATCH("배치 서버"),

}
