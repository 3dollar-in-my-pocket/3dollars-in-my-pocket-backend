package com.depromeet.threedollar.api.service.review.dto.response.deprecated;

import com.depromeet.threedollar.domain.collection.common.CursorSupporter;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.collection.store.StoreDictionary;
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
public class ReviewsCursorV2Response {

    private static final long LAST_CURSOR = -1L;

    private List<ReviewDetailV2Response> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private ReviewsCursorV2Response(List<ReviewDetailV2Response> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static ReviewsCursorV2Response of(CursorSupporter<Review> reviewsCursor, StoreDictionary storeDictionary, User user, long totalElements) {
        if (reviewsCursor.isLastCursor()) {
            return newLastCursor(reviewsCursor.getItemsInCurrentCursor(), storeDictionary, user, totalElements);
        }
        return newCursorHasNext(reviewsCursor.getItemsInCurrentCursor(), storeDictionary, user, totalElements, reviewsCursor.getNextCursor().getId());
    }

    private static ReviewsCursorV2Response newLastCursor(List<Review> reviews, StoreDictionary storeDictionary, User user, long totalElements) {
        return newCursorHasNext(reviews, storeDictionary, user, totalElements, LAST_CURSOR);
    }

    private static ReviewsCursorV2Response newCursorHasNext(List<Review> reviews, StoreDictionary storeDictionary, User user, long totalElements, long nextCursor) {
        List<ReviewDetailV2Response> contents = reviews.stream()
            .map(review -> ReviewDetailV2Response.of(review, storeDictionary.getStore(review.getStoreId()), user))
            .collect(Collectors.toList());
        return new ReviewsCursorV2Response(contents, totalElements, nextCursor);
    }

}
