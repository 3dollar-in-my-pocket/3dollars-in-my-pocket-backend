package com.depromeet.threedollar.api.adminservice.controller.userservice.review

import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.service.userservice.review.AdminUserReviewService
import com.depromeet.threedollar.api.adminservice.service.userservice.review.dto.response.ReviewInfoResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminUserReviewController(
    private val adminUserReviewService: AdminUserReviewService,
) {

    @ApiOperation("관리자가 특정 리뷰를 삭제한다")
    @Auth
    @DeleteMapping("/v1/user/review/{reviewId}")
    fun deleteUserReviewByForce(
        @PathVariable reviewId: Long,
    ): ApiResponse<String> {
        adminUserReviewService.deleteReviewByForce(reviewId)
        return ApiResponse.OK
    }

    @ApiOperation("관리자가 특정 리뷰 정보를 조회한다")
    @Auth
    @GetMapping("/v1/user/review/{reviewId}")
    fun getReviewInfo(
        @PathVariable reviewId: Long,
    ): ApiResponse<ReviewInfoResponse> {
        return ApiResponse.success(adminUserReviewService.getReviewInfo(reviewId))
    }

}
