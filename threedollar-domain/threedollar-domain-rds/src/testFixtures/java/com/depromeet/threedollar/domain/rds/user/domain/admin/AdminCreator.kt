package com.depromeet.threedollar.domain.rds.user.domain.admin

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object AdminCreator {

    @JvmStatic
    fun create(
        email: String,
        name: String,
    ): Admin {
        return Admin.builder()
            .email(email)
            .name(name)
            .build()
    }

}
