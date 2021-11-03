package com.depromeet.threedollar.api.controller.review;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.service.review.ReviewRetrieveService;
import com.depromeet.threedollar.api.service.review.ReviewService;
import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewScrollResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRetrieveService reviewRetrieveService;

    @ApiOperation("[인증] 가게 상세 페이지 - 가게에 새로운 리뷰를 등록합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PostMapping("/api/v2/store/review")
    public ApiResponse<ReviewInfoResponse> addStoreReview(@Valid @RequestBody AddReviewRequest request, @UserId Long userId) {
        return ApiResponse.success(reviewService.addReview(request, userId));
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 내가 작성한 리뷰를 수정합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PutMapping("/api/v2/store/review/{reviewId}")
    public ApiResponse<ReviewInfoResponse> updateStoreReview(@PathVariable Long reviewId, @Valid @RequestBody UpdateReviewRequest request,
                                                             @UserId Long userId) {
        return ApiResponse.success(reviewService.updateReview(reviewId, request, userId));
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 내가 작성한 리뷰를 삭제합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @DeleteMapping("/api/v2/store/review/{reviewId}")
    public ApiResponse<String> deleteStoreReview(@PathVariable Long reviewId, @UserId Long userId) {
        reviewService.deleteReview(reviewId, userId);
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 마이 페이지 - 내가 작성한 리뷰 목록을 스크롤 페이지네이션으로 조회합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @GetMapping("/api/v2/store/reviews/me")
    public ApiResponse<ReviewScrollResponse> retrieveMyStoreReviews(@Valid RetrieveMyReviewsRequest request, @UserId Long userId) {
        return ApiResponse.success(reviewRetrieveService.retrieveMyReviews(request, userId));
    }

}
