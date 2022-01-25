package com.depromeet.threedollar.boss.api.config.resolver

@Target(AnnotationTarget.FUNCTION)
annotation class Auth(
    val role: Role = Role.DEFAULT
)

enum class Role {

    DEFAULT,
    STORE_OWNER

}
