package com.depromeet.threedollar.domain.user.domain.user

import com.depromeet.threedollar.domain.user.domain.ObjectMother

@ObjectMother
object UserCreator {

    @JvmStatic
    fun create(
        socialId: String,
        socialType: UserSocialType,
        name: String
    ): User {
        return User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .name(name)
            .build()
    }

}
