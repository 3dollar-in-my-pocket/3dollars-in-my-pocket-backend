package com.depromeet.threedollar.domain.user.domain.medal

import com.depromeet.threedollar.domain.user.domain.TestFixture
import com.depromeet.threedollar.domain.user.domain.user.User

@TestFixture
object UserMedalCreator {

    @JvmStatic
    fun createActive(
        medal: Medal,
        user: User
    ): UserMedal {
        return UserMedal.builder()
            .medal(medal)
            .user(user)
            .status(UserMedalStatus.ACTIVE)
            .build()
    }

    @JvmStatic
    fun createInActive(
        medal: Medal,
        user: User
    ): UserMedal {
        return UserMedal.builder()
            .medal(medal)
            .user(user)
            .status(UserMedalStatus.IN_ACTIVE)
            .build()
    }

}
