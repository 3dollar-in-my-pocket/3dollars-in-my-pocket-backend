package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static ReviewScrollResponse of(ScrollPaginationCollection<ReviewWithWriterProjection> scrollCollection, Map<Long, Store> cachedStores) {
        if (scrollCollection.isLastScroll()) {
            return newLastScroll(scrollCollection.getItemsInCurrentScroll(), cachedStores);
        }
        return newScrollHasNext(scrollCollection.getItemsInCurrentScroll(), cachedStores, scrollCollection.getNextCursor().getReviewId());
    }

    private static ReviewScrollResponse newLastScroll(List<ReviewWithWriterProjection> reviews, Map<Long, Store> cachedStores) {
        return newScrollHasNext(reviews, cachedStores, LAST_CURSOR);
    }

    private static ReviewScrollResponse newScrollHasNext(List<ReviewWithWriterProjection> reviews, Map<Long, Store> cachedStores, long nextCursor) {
        List<ReviewDetailResponse> contents = reviews.stream()
            .map(review -> ReviewDetailResponse.of(review, cachedStores.get(review.getStoreId())))
            .collect(Collectors.toList());
        return new ReviewScrollResponse(contents, nextCursor);
    }

}
