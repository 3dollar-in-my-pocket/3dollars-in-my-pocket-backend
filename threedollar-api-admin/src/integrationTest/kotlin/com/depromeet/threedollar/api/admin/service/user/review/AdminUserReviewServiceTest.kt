package com.depromeet.threedollar.api.admin.service.user.review

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.admin.service.SetupAdminServiceTest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewStatus

internal class AdminUserReviewServiceTest(
    private val adminUserReviewService: AdminUserReviewService,
    private val reviewRepository: ReviewRepository
) : SetupAdminServiceTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        reviewRepository.deleteAll()
    }

    @Test
    fun `특정 리뷰를 강제로 삭제한다`() {
        // given
        val review = ReviewCreator.builder()
            .storeId(10000L)
            .userId(1L)
            .contents("욕설")
            .rating(1)
            .build()
        reviewRepository.save(review)

        // when
        adminUserReviewService.deleteReviewByForce(reviewId = review.id)

        // then
        val reviews = reviewRepository.findAll()
        assertAll({
            assertThat(reviews).hasSize(1)
            reviews[0].let {
                assertThat(it.status).isEqualTo(ReviewStatus.FILTERED)

                assertThat(it.id).isEqualTo(review.id)
                assertThat(it.contents).isEqualTo(review.contents)
                assertThat(it.rating).isEqualTo(review.rating)
                assertThat(it.userId).isEqualTo(review.userId)
            }
        })
    }

    @Test
    fun `리뷰 삭제시 해당하는 리뷰가 존재하지 않는 경우 NotFoundException`() {
        // when & then
        assertThatThrownBy { adminUserReviewService.deleteReviewByForce(reviewId = -1L) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `리뷰 삭제시 해당하는 리뷰가 유저에 의해 삭제처리된 리뷰인 경우 NotFoundException`() {
        // given
        val review = ReviewCreator.builder()
            .storeId(10000L)
            .userId(1L)
            .contents("욕설")
            .rating(1)
            .build()
        reviewRepository.save(review)

        // when & then
        assertThatThrownBy { adminUserReviewService.deleteReviewByForce(reviewId = -1L) }.isInstanceOf(NotFoundException::class.java)
    }

}
