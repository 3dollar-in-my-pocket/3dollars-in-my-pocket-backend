package com.depromeet.threedollar.api.controller.review;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.review.ReviewRetrieveService;
import com.depromeet.threedollar.api.service.review.ReviewService;
import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.user.event.review.ReviewChangedEvent;
import com.depromeet.threedollar.domain.user.event.review.ReviewCreatedEvent;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRetrieveService reviewRetrieveService;
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation("[인증] 가게 상세 페이지 - 가게에 새로운 리뷰를 등록합니다")
    @Auth
    @PostMapping("/v2/store/review")
    public ApiResponse<ReviewInfoResponse> addReview(
        @Valid @RequestBody AddReviewRequest request,
        @UserId Long userId
    ) {
        ReviewInfoResponse response = reviewService.addReview(request, userId);
        eventPublisher.publishEvent(ReviewCreatedEvent.of(response.getReviewId(), userId));
        eventPublisher.publishEvent(ReviewChangedEvent.of(request.getStoreId()));
        return ApiResponse.success(response);
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 내가 작성한 리뷰를 수정합니다")
    @Auth
    @PutMapping("/v2/store/review/{reviewId}")
    public ApiResponse<ReviewInfoResponse> updateReview(
        @PathVariable Long reviewId,
        @Valid @RequestBody UpdateReviewRequest request,
        @UserId Long userId
    ) {
        ReviewInfoResponse response = reviewService.updateReview(reviewId, request, userId);
        eventPublisher.publishEvent(ReviewChangedEvent.of(response.getStoreId()));
        return ApiResponse.success(response);
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 내가 작성한 리뷰를 삭제합니다")
    @Auth
    @DeleteMapping("/v2/store/review/{reviewId}")
    public ApiResponse<String> deleteReview(
        @PathVariable Long reviewId,
        @UserId Long userId
    ) {
        ReviewInfoResponse response = reviewService.deleteReview(reviewId, userId);
        eventPublisher.publishEvent(ReviewChangedEvent.of(response.getStoreId()));
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 마이 페이지 - 내가 작성한 리뷰 목록을 스크롤 페이지네이션으로 조회합니다 (삭제된 가게 포함 O)")
    @Auth
    @GetMapping("/v3/store/reviews/me")
    public ApiResponse<ReviewsCursorResponse> retrieveMyReviewHistories(
        @Valid RetrieveMyReviewsRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(reviewRetrieveService.retrieveMyReviewHistories(request, userId));
    }

}
