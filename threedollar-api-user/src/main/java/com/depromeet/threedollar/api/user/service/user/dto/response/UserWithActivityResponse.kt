package com.depromeet.threedollar.api.user.service.user.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.user.domain.user.User
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType

data class UserWithActivityResponse(
    val userId: Long,
    val name: String,
    val socialType: UserSocialType,
    val medal: UserMedalResponse?,
    val activity: ActivityResponse,
) : AuditingTimeResponse() {

    companion object {
        fun of(
            user: User,
            storesCount: Long,
            reviewsCount: Long,
        ): UserWithActivityResponse {
            val response = UserWithActivityResponse(
                userId = user.id,
                name = user.name,
                socialType = user.socialType,
                medal = UserMedalResponse.of(user.activatedMedal),
                activity = ActivityResponse(storesCount, reviewsCount, user.userMedals.size
                )
            )
            response.setAuditingTimeByEntity(user)
            return response
        }
    }

}


data class ActivityResponse(
    val storesCount: Long,
    val reviewsCount: Long,
    val medalsCounts: Int,
)
