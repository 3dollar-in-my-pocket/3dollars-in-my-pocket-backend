package com.depromeet.threedollar.api.admin.service.user.review.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.user.domain.user.User
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType


data class UserInfoResponse(
    val userId: Long?,
    val socialType: UserSocialType?,
    val name: String
) : AuditingTimeResponse() {

    companion object {
        fun of(user: User): UserInfoResponse {
            val response = UserInfoResponse(
                userId = user.id,
                socialType = user.socialType,
                name = user.name
            )
            response.setAuditingTimeByEntity(user)
            return response
        }
    }

}