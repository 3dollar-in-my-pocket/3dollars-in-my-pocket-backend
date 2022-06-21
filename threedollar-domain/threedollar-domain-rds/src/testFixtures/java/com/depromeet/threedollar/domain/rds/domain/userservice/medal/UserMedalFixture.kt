package com.depromeet.threedollar.domain.rds.domain.userservice.medal

import com.depromeet.threedollar.domain.rds.domain.TestFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User

@TestFixture
object UserMedalFixture {

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
