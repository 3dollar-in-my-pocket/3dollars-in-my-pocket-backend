package com.depromeet.threedollar.domain.rds.user.event.review;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewCreatedEvent {

    private final Long reviewId;

    private final Long userId;

    public static ReviewCreatedEvent of(Long reviewId, Long userId) {
        return new ReviewCreatedEvent(reviewId, userId);
    }

}
