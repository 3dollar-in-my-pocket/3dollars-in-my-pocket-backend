package com.depromeet.threedollar.domain.rds.domain.userservice.review

import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object ReviewFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        storeId: Long = 300000L,
        userId: Long = 200000L,
        contents: String = "붕어빵이 너무 맛있어요\n가격도 나쁘지 않아요",
        rating: Int = 5,
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
