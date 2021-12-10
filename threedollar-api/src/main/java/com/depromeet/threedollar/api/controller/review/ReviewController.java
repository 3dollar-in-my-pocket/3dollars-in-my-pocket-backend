package com.depromeet.threedollar.api.controller.review;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.review.ReviewRetrieveService;
import com.depromeet.threedollar.api.service.review.ReviewService;
import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.deprecated.RetrieveMyReviewsV2Request;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewScrollResponse;
import com.depromeet.threedollar.api.service.review.dto.response.deprecated.ReviewScrollV2Response;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.event.review.ReviewChangedEvent;
import com.depromeet.threedollar.domain.event.review.ReviewCreatedEvent;
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
    @PostMapping("/api/v2/store/review")
    public ApiResponse<ReviewInfoResponse> addReview(@Valid @RequestBody AddReviewRequest request, @UserId Long userId) {
        ReviewInfoResponse response = reviewService.addReview(request, userId);
        eventPublisher.publishEvent(ReviewCreatedEvent.of(response.getReviewId(), userId));
        eventPublisher.publishEvent(ReviewChangedEvent.of(request.getStoreId()));
        return ApiResponse.success(response);
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 내가 작성한 리뷰를 수정합니다")
    @Auth
    @PutMapping("/api/v2/store/review/{reviewId}")
    public ApiResponse<ReviewInfoResponse> updateReview(@PathVariable Long reviewId, @Valid @RequestBody UpdateReviewRequest request,
                                                        @UserId Long userId) {
        ReviewInfoResponse response = reviewService.updateReview(reviewId, request, userId);
        eventPublisher.publishEvent(ReviewChangedEvent.of(response.getStoreId()));
        return ApiResponse.success(response);
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 내가 작성한 리뷰를 삭제합니다")
    @Auth
    @DeleteMapping("/api/v2/store/review/{reviewId}")
    public ApiResponse<String> deleteReview(@PathVariable Long reviewId, @UserId Long userId) {
        ReviewInfoResponse response = reviewService.deleteReview(reviewId, userId);
        eventPublisher.publishEvent(ReviewChangedEvent.of(response.getStoreId()));
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 마이 페이지 - 내가 작성한 리뷰 목록을 스크롤 페이지네이션으로 조회합니다 (삭제된 가게 포함 O)")
    @Auth
    @GetMapping("/api/v3/store/reviews/me")
    public ApiResponse<ReviewScrollResponse> retrieveMyReviewHistories(@Valid RetrieveMyReviewsRequest request, @UserId Long userId) {
        return ApiResponse.success(reviewRetrieveService.retrieveMyReviewHistories(request, userId));
    }

    /**
     * v2.1.1 부터 Deprecated
     * 내가 작성한 리뷰 조회시, 삭제된 가게들을 반환하되, 삭제된 가게라고 표기해줘야하는 이슈에 대응하기 위함. (호환성을 유지하기 위한 API)
     * use GET /api/v3/store/reviews/me
     */
    @Deprecated
    @ApiOperation("[인증] 마이 페이지 - 내가 작성한 리뷰 목록을 스크롤 페이지네이션으로 조회합니다 (삭제된 가게 포함 X)")
    @Auth
    @GetMapping("/api/v2/store/reviews/me")
    public ApiResponse<ReviewScrollV2Response> retrieveMyReviewHistoriesV2(@Valid RetrieveMyReviewsV2Request request, @UserId Long userId) {
        return ApiResponse.success(reviewRetrieveService.retrieveMyReviewHistoriesV2(request, userId));
    }

}
