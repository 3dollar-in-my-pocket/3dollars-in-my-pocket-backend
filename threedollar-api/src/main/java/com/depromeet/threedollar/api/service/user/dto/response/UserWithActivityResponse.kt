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
            reportedStoresCount: Long,
            writtenReviewsCount: Long,
            ownedMedalsCounts: Long
        ): UserWithActivityResponse {
            return UserWithActivityResponse(
                user.id, user.name, user.socialType, user.medalType,
                ActivityResponse(reportedStoresCount, writtenReviewsCount, ownedMedalsCounts)
            )
        }
    }

}


data class ActivityResponse(
    val reportedStoresCount: Long,
    val writtenReviewsCount: Long,
    val ownedMedalsCounts: Long
)
