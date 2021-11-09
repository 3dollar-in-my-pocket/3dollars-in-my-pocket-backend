package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewScrollResponse {

    private List<ReviewDetailResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private ReviewScrollResponse(List<ReviewDetailResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static ReviewScrollResponse newLastScroll(List<ReviewWithWriterProjection> reviews, long totalElements, Map<Long, Store> cachedStores) {
        return newScrollHasNext(reviews, totalElements, cachedStores, -1L);
    }

    public static ReviewScrollResponse newScrollHasNext(List<ReviewWithWriterProjection> reviews, long totalElements, Map<Long, Store> cachedStores, long nextCursor) {
        List<ReviewDetailResponse> contents = reviews.stream()
            .map(review -> ReviewDetailResponse.of(review, cachedStores.get(review.getStoreId())))
            .collect(Collectors.toList());
        return new ReviewScrollResponse(contents, totalElements, nextCursor);
    }

}
