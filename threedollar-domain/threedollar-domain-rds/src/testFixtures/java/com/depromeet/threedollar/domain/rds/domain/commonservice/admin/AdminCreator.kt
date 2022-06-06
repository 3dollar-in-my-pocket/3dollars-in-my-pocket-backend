package com.depromeet.threedollar.domain.rds.domain.commonservice.admin

import com.depromeet.threedollar.domain.rds.domain.TestFixture

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
