package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewInfoResponse extends AuditingTimeResponse {

    private Long reviewId;
    private Long storeId;
    private String contents;
    private int rating;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewInfoResponse(Long reviewId, Long storeId, String contents, int rating) {
        this.reviewId = reviewId;
        this.storeId = storeId;
        this.contents = contents;
        this.rating = rating;
    }

    public static ReviewInfoResponse of(Review review) {
        ReviewInfoResponse response = ReviewInfoResponse.builder()
            .reviewId(review.getId())
            .storeId(review.getStoreId())
            .contents(review.getContents())
            .rating(review.getRating())
            .build();
        response.setBaseTime(review);
        return response;
    }

    public static ReviewInfoResponse of(ReviewWithWriterProjection review) {
        ReviewInfoResponse response = ReviewInfoResponse.builder()
            .reviewId(review.getReviewId())
            .storeId(review.getStoreId())
            .contents(review.getContents())
            .rating(review.getRating())
            .build();
        response.setBaseTime(review.getCreatedAt(), review.getUpdatedAt());
        return response;
    }

}
