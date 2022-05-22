package com.depromeet.threedollar.domain.rds.user.domain.medal

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture
import com.depromeet.threedollar.domain.rds.user.domain.user.User

@TestFixture
object UserMedalCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        medal: Medal,
        user: User,
        status: UserMedalStatus = UserMedalStatus.ACTIVE,
    ): UserMedal {
        return UserMedal.builder()
            .medal(medal)
            .user(user)
            .status(status)
            .build()
    }

}
