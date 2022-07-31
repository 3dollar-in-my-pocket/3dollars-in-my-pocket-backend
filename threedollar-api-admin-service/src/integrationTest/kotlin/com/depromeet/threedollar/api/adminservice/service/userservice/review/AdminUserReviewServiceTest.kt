package com.depromeet.threedollar.api.adminservice.service.userservice.review

import com.depromeet.threedollar.api.adminservice.SetupUserStoreIntegrationTest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class AdminUserReviewServiceTest(
    private val adminUserReviewService: AdminUserReviewService,
    private val reviewRepository: ReviewRepository,
) : SetupUserStoreIntegrationTest() {

    @Test
    fun `특정 리뷰를 강제로 삭제한다`() {
        // given
        val review = ReviewFixture.create(storeId = storeId)
        reviewRepository.save(review)

        // when
        adminUserReviewService.deleteReviewByForce(reviewId = review.id)

        // then
        val reviews = reviewRepository.findAll()
        assertAll({
            assertThat(reviews).hasSize(1)
            assertReview(
                review = reviews[0],
                status = ReviewStatus.FILTERED,
                reviewId = review.id,
                contents = review.contents,
                rating = review.rating,
                userId = review.userId
            )
        })
    }

    @Test
    fun `리뷰를 강제로 삭제시 가게의 평균 점수가 재계산 된다`() {
        // given
        val review = ReviewFixture.create(storeId = storeId, rating = 4)
        reviewRepository.save(review)

        // when
        adminUserReviewService.deleteReviewByForce(reviewId = review.id)

        // then
        val stores = storeRepository.findAll()
        assertThat(stores).hasSize(1)
        assertThat(stores[0].rating).isEqualTo(0.0)
    }

    @Test
    fun `리뷰 삭제시 해당하는 리뷰가 존재하지 않는 경우 NotFoundException`() {
        // when & then
        assertThatThrownBy { adminUserReviewService.deleteReviewByForce(reviewId = -1L) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `리뷰 삭제시 해당하는 리뷰가 유저에 의해 삭제처리된 리뷰인 경우 NotFoundException`() {
        // given
        val review = ReviewFixture.create(status = ReviewStatus.DELETED)
        reviewRepository.save(review)

        // when & then
        assertThatThrownBy { adminUserReviewService.deleteReviewByForce(reviewId = review.id) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun assertReview(review: Review, status: ReviewStatus, reviewId: Long, contents: String, rating: Int, userId: Long) {
        assertThat(review.status).isEqualTo(status)
        assertThat(review.id).isEqualTo(reviewId)
        assertThat(review.contents).isEqualTo(contents)
        assertThat(review.rating).isEqualTo(rating)
        assertThat(review.userId).isEqualTo(userId)
    }

}
