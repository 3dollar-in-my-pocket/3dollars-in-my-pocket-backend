package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.user.domain.review.Review;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewInfoResponse extends AuditingTimeResponse {

    private Long reviewId;

    private Long storeId;

    @Nullable
    private String contents;

    private int rating;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewInfoResponse(Long reviewId, Long storeId, @Nullable String contents, int rating) {
        this.reviewId = reviewId;
        this.storeId = storeId;
        this.contents = contents;
        this.rating = rating;
    }

    public static ReviewInfoResponse of(@NotNull Review review) {
        ReviewInfoResponse response = ReviewInfoResponse.builder()
            .reviewId(review.getId())
            .storeId(review.getStoreId())
            .contents(review.getContents())
            .rating(review.getRating())
            .build();
        response.setBaseTime(review);
        return response;
    }

}
