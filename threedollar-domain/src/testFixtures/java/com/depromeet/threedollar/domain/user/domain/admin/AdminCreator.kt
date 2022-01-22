package com.depromeet.threedollar.domain.user.domain.admin

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object AdminCreator {

    @JvmStatic
    fun create(
        email: String,
        name: String
    ): Admin {
        return Admin.builder()
            .email(email)
            .name(name)
            .build()
    }

}
