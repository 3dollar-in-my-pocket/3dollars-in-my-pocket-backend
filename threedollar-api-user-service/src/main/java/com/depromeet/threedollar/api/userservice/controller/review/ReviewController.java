package com.depromeet.threedollar.api.userservice.controller.review;

import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth;
import com.depromeet.threedollar.api.userservice.config.resolver.UserId;
import com.depromeet.threedollar.api.userservice.service.review.ReviewRetrieveService;
import com.depromeet.threedollar.api.userservice.service.review.ReviewService;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.domain.rds.event.userservice.review.ReviewChangedEvent;
import com.depromeet.threedollar.domain.rds.event.userservice.review.ReviewCreatedEvent;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRetrieveService reviewRetrieveService;
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation("[인증] 가게에 새로운 리뷰를 등록합니다")
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

    @ApiOperation("[인증] 내가 작성한 리뷰를 수정합니다")
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

    @ApiOperation("[인증] 내가 작성한 리뷰를 삭제합니다")
    @Auth
    @DeleteMapping("/v2/store/review/{reviewId}")
    public ApiResponse<String> deleteReview(
        @PathVariable Long reviewId,
        @UserId Long userId
    ) {
        ReviewInfoResponse response = reviewService.deleteReview(reviewId, userId);
        eventPublisher.publishEvent(ReviewChangedEvent.of(response.getStoreId()));
        return ApiResponse.OK;
    }

    @ApiOperation(value = "[인증] 내가 작성한 리뷰 목록을 스크롤 페이지네이션으로 조회합니다", notes = "[스크롤 페이지네이션] 첫 스크롤 조회시 cursor=null 그 이후부터 nextCursor로 응답하는 id를 cursor로 hasNext가 false일때 까지 반복 조회")
    @Auth
    @GetMapping("/v3/store/reviews/me")
    public ApiResponse<ReviewsCursorResponse> retrieveMyReviewHistories(
        @Valid RetrieveMyReviewsRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(reviewRetrieveService.retrieveMyReviewHistories(request, userId));
    }

}
