package com.depromeet.threedollar.domain.rds.vendor.domain.user

import com.depromeet.threedollar.domain.rds.vendor.domain.TestFixture

@TestFixture
object UserCreator {

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
