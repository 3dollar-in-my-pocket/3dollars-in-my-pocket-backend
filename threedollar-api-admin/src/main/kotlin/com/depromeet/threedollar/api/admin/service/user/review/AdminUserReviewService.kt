package com.depromeet.threedollar.api.admin.service.user.review

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository

@Service
class AdminUserReviewService(
    private val reviewRepository: ReviewRepository
) {

    @Transactional
    fun deleteReviewByForce(reviewId: Long) {
        val review = reviewRepository.findReviewById(reviewId)
            ?: throw NotFoundException("해당하는 리뷰($reviewId)는 존재하지 않습니다", ErrorCode.NOTFOUND_REVIEW)
        review.deleteByAdmin()
    }

}
