package com.depromeet.threedollar.domain.rds.domain.userservice.user

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object UserFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        socialId: String = "social-id",
        socialType: UserSocialType = UserSocialType.APPLE,
        name: String = "닉네임",
    ): User {
        return User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .name(name)
            .build()
    }

}
