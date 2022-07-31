package com.depromeet.threedollar.api.userservice.service.review.dto.response;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.collection.ReviewPagingCursor;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreDictionary;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewsCursorResponse {

    private static final long LAST_CURSOR = -1L;

    private List<ReviewDetailResponse> contents = new ArrayList<>();
    private long nextCursor;
    private boolean hasNext;

    private ReviewsCursorResponse(List<ReviewDetailResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
        this.hasNext = LAST_CURSOR != nextCursor;
    }

    public static ReviewsCursorResponse of(@NotNull ReviewPagingCursor reviewsPagingCursor, @NotNull StoreDictionary storeDictionary, @NotNull User user) {
        List<ReviewDetailResponse> reviews = combineReviewDetailResponse(reviewsPagingCursor.getCurrentCursorItems(), storeDictionary, user);
        if (reviewsPagingCursor.hasNext()) {
            return new ReviewsCursorResponse(reviews, reviewsPagingCursor.getNextCursor().getId());
        }
        return new ReviewsCursorResponse(reviews, LAST_CURSOR);
    }

    private static List<ReviewDetailResponse> combineReviewDetailResponse(List<Review> reviews, StoreDictionary storeDictionary, User user) {
        return reviews.stream()
            .map(review -> ReviewDetailResponse.of(review, storeDictionary.getStore(review.getStoreId()), user))
            .collect(Collectors.toList());
    }

}
