package com.depromeet.threedollar.api.adminservice.service.userservice.review

import com.depromeet.threedollar.api.adminservice.service.userservice.review.dto.response.ReviewInfoResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminUserReviewService(
    private val reviewRepository: ReviewRepository,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun deleteReviewByForce(reviewId: Long) {
        val review = findReviewById(reviewRepository, reviewId)
        review.deleteByAdmin()
    }

    @Transactional(readOnly = true)
    fun getReviewInfo(reviewId: Long): ReviewInfoResponse {
        val review: Review = findReviewById(reviewRepository, reviewId)
        return ReviewInfoResponse.of(
            review = review,
            store = storeRepository.findStoreById(review.storeId),
            user = userRepository.findUserById(review.userId),
        )
    }

}

private fun findReviewById(reviewRepository: ReviewRepository, reviewId: Long): Review {
    return reviewRepository.findReviewById(reviewId)
        ?: throw NotFoundException("해당하는 리뷰($reviewId)는 존재하지 않습니다", ErrorCode.E404_NOT_EXISTS_REVIEW)
}
