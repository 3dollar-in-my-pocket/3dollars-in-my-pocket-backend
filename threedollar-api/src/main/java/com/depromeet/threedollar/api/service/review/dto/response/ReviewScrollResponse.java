package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.domain.collection.common.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.collection.store.StoreCacheCollection;
import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewScrollResponse {

    private static final long LAST_CURSOR = -1L;

    private List<ReviewDetailResponse> contents = new ArrayList<>();
    private long nextCursor;

    private ReviewScrollResponse(List<ReviewDetailResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
    }

    public static ReviewScrollResponse of(ScrollPaginationCollection<Review> reviewsScroll, StoreCacheCollection stores, User user) {
        if (reviewsScroll.isLastScroll()) {
            return newLastScroll(reviewsScroll.getCurrentScrollItems(), stores, user);
        }
        return newScrollHasNext(reviewsScroll.getCurrentScrollItems(), stores, user, reviewsScroll.getNextCursor().getId());
    }

    private static ReviewScrollResponse newLastScroll(List<Review> reviews, StoreCacheCollection stores, User user) {
        return newScrollHasNext(reviews, stores, user, LAST_CURSOR);
    }

    private static ReviewScrollResponse newScrollHasNext(List<Review> reviews, StoreCacheCollection stores, User user, long nextCursor) {
        List<ReviewDetailResponse> contents = reviews.stream()
            .map(review -> ReviewDetailResponse.of(review, stores.getStore(review.getStoreId()), user))
            .collect(Collectors.toList());
        return new ReviewScrollResponse(contents, nextCursor);
    }

}
