package com.depromeet.threedollar.domain.rds.user.domain.user

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object WithdrawalUserCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
            socialId: String,
            socialType: UserSocialType,
            userId: Long,
            name: String = "가삼",
    ): WithdrawalUser {
        return WithdrawalUser.builder()
            .userId(userId)
            .socialInfo(SocialInfo.of(socialId, socialType))
            .name(name)
            .build()
    }

}
