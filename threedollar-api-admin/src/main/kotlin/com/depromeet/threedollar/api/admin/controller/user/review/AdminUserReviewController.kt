package com.depromeet.threedollar.api.admin.controller.user.review

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.user.review.AdminUserReviewService
import com.depromeet.threedollar.api.admin.service.user.review.dto.response.ReviewInfoResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation

@RestController
class AdminUserReviewController(
    private val adminUserReviewService: AdminUserReviewService
) {

    @ApiOperation("관리자가 특정 리뷰를 삭제한다")
    @Auth
    @DeleteMapping("/v1/user/review/{reviewId}")
    fun deleteUserReviewByForce(
        @PathVariable reviewId: Long
    ): ApiResponse<String> {
        adminUserReviewService.deleteReviewByForce(reviewId)
        return ApiResponse.OK
    }

    @ApiOperation("관리자가 특정 리뷰 정보를 조회한다")
    @Auth
    @GetMapping("/v1/user/review/{reviewId}")
    fun getReviewInfo(
        @PathVariable reviewId: Long
    ): ApiResponse<ReviewInfoResponse> {
        return ApiResponse.success(adminUserReviewService.getReviewInfo(reviewId))
    }

}
