package com.depromeet.threedollar.domain.rds.domain.userservice.user

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object UserFixture {

    @JvmStatic
    fun create(
        socialId: String,
        socialType: UserSocialType,
        name: String,
    ): User {
        return User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .name(name)
            .build()
    }

}
