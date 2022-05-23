package com.depromeet.threedollar.domain.rds.vendor.domain.admin

import com.depromeet.threedollar.domain.rds.vendor.domain.TestFixture

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
