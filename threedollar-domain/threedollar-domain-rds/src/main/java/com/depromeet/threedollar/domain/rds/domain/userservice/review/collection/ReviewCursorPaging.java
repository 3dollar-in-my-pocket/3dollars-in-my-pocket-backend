package com.depromeet.threedollar.domain.rds.domain.userservice.review.collection;

import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewCursorPaging {

    private final CursorPagingSupporter<Review> reviewCursor;

    private ReviewCursorPaging(List<Review> reviewsWithNextCursor, int size) {
        this.reviewCursor = CursorPagingSupporter.of(reviewsWithNextCursor, size);
    }

    public static ReviewCursorPaging of(List<Review> reviewsWithNextCursor, int size) {
        return new ReviewCursorPaging(reviewsWithNextCursor, size);
    }

    public boolean hasNext() {
        return reviewCursor.hasNext();
    }

    public List<Review> getCurrentCursorItems() {
        return reviewCursor.getCurrentCursorItems();
    }

    @NotNull
    public Review getNextCursor() {
        return reviewCursor.getNextCursor();
    }

    public List<Long> getStoreIds() {
        return reviewCursor.getCurrentCursorItems().stream()
            .map(Review::getStoreId)
            .collect(Collectors.toList());
    }

}
