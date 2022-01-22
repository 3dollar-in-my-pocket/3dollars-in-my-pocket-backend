package com.depromeet.threedollar.domain.user.domain.medal

import com.depromeet.threedollar.domain.user.domain.ObjectMother
import com.depromeet.threedollar.domain.user.domain.user.User

@ObjectMother
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
