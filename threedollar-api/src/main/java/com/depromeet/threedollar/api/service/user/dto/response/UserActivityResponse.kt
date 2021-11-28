package com.depromeet.threedollar.api.service.user.dto.response

import com.depromeet.threedollar.domain.domain.user.User

data class UserActivityResponse(
    val user: UserInfoResponse,
    val activity: ActivityResponse
) {

    companion object {
        fun of(
            user: User,
            reportedStoresCount: Long,
            writtenReviewsCount: Long,
            ownedMedalsCounts: Long
        ): UserActivityResponse {
            return UserActivityResponse(
                UserInfoResponse.of(user),
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
