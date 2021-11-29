package com.depromeet.threedollar.api.service.review.dto.response.deprecated;

import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static ReviewScrollV2Response of(ScrollPaginationCollection<ReviewWithWriterProjection> scrollCollection, Map<Long, Store> cachedStores, long totalElements) {
        if (scrollCollection.isLastScroll()) {
            return newLastScroll(scrollCollection.getItemsInCurrentScroll(), cachedStores, totalElements);
        }
        return newScrollHasNext(scrollCollection.getItemsInCurrentScroll(), cachedStores, totalElements, scrollCollection.getNextCursor().getReviewId());
    }

    private static ReviewScrollV2Response newLastScroll(List<ReviewWithWriterProjection> reviews, Map<Long, Store> cachedStores, long totalElements) {
        return newScrollHasNext(reviews, cachedStores, totalElements, LAST_CURSOR);
    }

    private static ReviewScrollV2Response newScrollHasNext(List<ReviewWithWriterProjection> reviews, Map<Long, Store> cachedStores, long totalElements, long nextCursor) {
        List<ReviewDetailV2Response> contents = reviews.stream()
            .map(review -> ReviewDetailV2Response.of(review, cachedStores.get(review.getStoreId())))
            .collect(Collectors.toList());
        return new ReviewScrollV2Response(contents, totalElements, nextCursor);
    }

}
