package com.depromeet.threedollar.domain.rds.domain.userservice.review.support;

import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewPagingCursor {

    private final CursorPagingSupporter<Review> reviewCursor;

    private ReviewPagingCursor(List<Review> reviewsWithNextCursor, int size) {
        this.reviewCursor = CursorPagingSupporter.of(reviewsWithNextCursor, size);
    }

    public static ReviewPagingCursor of(List<Review> reviewsWithNextCursor, int size) {
        return new ReviewPagingCursor(reviewsWithNextCursor, size);
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
