package com.depromeet.threedollar.domain.rds.user.domain.medal

import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User
import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

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
