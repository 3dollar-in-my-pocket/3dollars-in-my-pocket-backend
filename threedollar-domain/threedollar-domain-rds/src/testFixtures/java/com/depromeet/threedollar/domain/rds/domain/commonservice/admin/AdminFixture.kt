package com.depromeet.threedollar.domain.rds.domain.commonservice.admin

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object AdminFixture {

    @JvmStatic
    fun create(
        email: String = "admin@gmail.com",
        name: String = "관리자 이름",
    ): Admin {
        return Admin.builder()
            .email(email)
            .name(name)
            .build()
    }

}
