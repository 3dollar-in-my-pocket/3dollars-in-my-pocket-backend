package com.depromeet.threedollar.api.service.review.dto.response.deprecated;

import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.store.StoreCollection;
import com.depromeet.threedollar.domain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewScrollV2Response {

    private static final long LAST_CURSOR = -1L;

    private List<ReviewDetailV2Response> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private ReviewScrollV2Response(List<ReviewDetailV2Response> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static ReviewScrollV2Response of(ScrollPaginationCollection<Review> scrollCollection, StoreCollection storeCollection, User user, long totalElements) {
        if (scrollCollection.isLastScroll()) {
            return newLastScroll(scrollCollection.getItemsInCurrentScroll(), storeCollection, user, totalElements);
        }
        return newScrollHasNext(scrollCollection.getItemsInCurrentScroll(), storeCollection, user, totalElements, scrollCollection.getNextCursor().getId());
    }

    private static ReviewScrollV2Response newLastScroll(List<Review> reviews, StoreCollection storeCollection, User user, long totalElements) {
        return newScrollHasNext(reviews, storeCollection, user, totalElements, LAST_CURSOR);
    }

    private static ReviewScrollV2Response newScrollHasNext(List<Review> reviews, StoreCollection storeCollection, User user, long totalElements, long nextCursor) {
        List<ReviewDetailV2Response> contents = reviews.stream()
            .map(review -> ReviewDetailV2Response.of(review, storeCollection.getStore(review.getStoreId()), user))
            .collect(Collectors.toList());
        return new ReviewScrollV2Response(contents, totalElements, nextCursor);
    }

}
