package com.depromeet.threedollar.api.service.user.dto.response

import com.depromeet.threedollar.api.service.medal.dto.response.UserMedalResponse
import com.depromeet.threedollar.domain.domain.user.User
import com.depromeet.threedollar.domain.domain.user.UserSocialType

data class UserWithActivityResponse(
    val userId: Long,
    val name: String,
    val socialType: UserSocialType,
    val medal: UserMedalResponse?,
    val activity: ActivityResponse
) {

    companion object {
        fun of(
            user: User,
            storesCount: Long,
            reviewsCount: Long
        ): UserWithActivityResponse {
            return UserWithActivityResponse(
                user.id, user.name, user.socialType,
                UserMedalResponse.of(user.activeMedal),
                ActivityResponse(storesCount, reviewsCount, user.userMedals.size)
            )
        }
    }

}


data class ActivityResponse(
    val storesCount: Long,
    val reviewsCount: Long,
    val medalsCounts: Int
)
