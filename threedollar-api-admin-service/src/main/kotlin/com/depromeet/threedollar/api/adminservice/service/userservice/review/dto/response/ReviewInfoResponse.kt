package com.depromeet.threedollar.api.adminservice.service.userservice.review.dto.response

import com.depromeet.threedollar.api.adminservice.service.userservice.store.dto.response.StoreInfoResponse
import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User

data class ReviewInfoResponse(
    val reviewId: Long,
    val contents: String,
    val rating: Int,
    val user: UserInfoResponse?,
    val store: StoreInfoResponse?,
) : AuditingTimeResponse() {

    companion object {
        fun of(review: Review, store: Store?, user: User?): ReviewInfoResponse {
            val response = ReviewInfoResponse(
                reviewId = review.id,
                contents = review.contents,
                rating = review.rating,
                user = user?.let { UserInfoResponse.of(it) },
                store = store?.let { StoreInfoResponse.of(it) }
            )
            response.setAuditingTimeByEntity(review)
            return response
        }
    }

}


