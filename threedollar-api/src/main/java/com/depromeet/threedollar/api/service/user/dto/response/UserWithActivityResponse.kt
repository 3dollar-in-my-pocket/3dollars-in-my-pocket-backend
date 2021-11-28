package com.depromeet.threedollar.api.service.user.dto.response

import com.depromeet.threedollar.domain.domain.medal.UserMedalType
import com.depromeet.threedollar.domain.domain.user.User
import com.depromeet.threedollar.domain.domain.user.UserSocialType

data class UserWithActivityResponse(
    val userId: Long,
    val name: String,
    val socialType: UserSocialType,
    val medalType: UserMedalType?,
    val activity: ActivityResponse
) {

    companion object {
        fun of(
            user: User,
            storesCount: Long,
            reviewsCount: Long,
            medalsCounts: Long
        ): UserWithActivityResponse {
            return UserWithActivityResponse(
                user.id, user.name, user.socialType, user.medalType,
                ActivityResponse(storesCount, reviewsCount, medalsCounts)
            )
        }
    }

}


data class ActivityResponse(
    val storesCount: Long,
    val reviewsCount: Long,
    val medalsCounts: Long
)
