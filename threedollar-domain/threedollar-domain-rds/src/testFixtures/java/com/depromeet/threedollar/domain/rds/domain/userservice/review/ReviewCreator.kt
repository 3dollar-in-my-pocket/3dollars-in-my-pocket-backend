package com.depromeet.threedollar.domain.rds.domain.userservice.review

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object ReviewCreator {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: Long,
        userId: Long,
        contents: String,
        rating: Int,
        status: ReviewStatus = ReviewStatus.POSTED,
    ): Review {
        return Review.builder()
            .storeId(storeId)
            .userId(userId)
            .contents(contents)
            .rating(rating)
            .status(status)
            .build()
    }

}
