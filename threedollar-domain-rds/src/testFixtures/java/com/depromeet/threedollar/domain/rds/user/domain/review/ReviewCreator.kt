package com.depromeet.threedollar.domain.rds.user.domain.review

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object ReviewCreator {

    @JvmStatic
    fun create(
        storeId: Long,
        userId: Long,
        contents: String,
        rating: Int
    ): Review {
        return Review.builder()
            .storeId(storeId)
            .userId(userId)
            .contents(contents)
            .rating(rating)
            .status(ReviewStatus.POSTED)
            .build()
    }

    @JvmStatic
    fun createDeleted(
        storeId: Long,
        userId: Long,
        contents: String,
        rating: Int
    ): Review {
        val review = Review.builder()
            .storeId(storeId)
            .userId(userId)
            .contents(contents)
            .rating(rating)
            .status(ReviewStatus.DELETED)
            .build()
        review.delete()
        return review
    }

}
