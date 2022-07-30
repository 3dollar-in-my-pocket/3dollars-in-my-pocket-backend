package com.depromeet.threedollar.domain.rds.event.userservice.review;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewCreatedEvent {

    private final Long reviewId;

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewCreatedEvent(Long reviewId, Long userId) {
        this.reviewId = reviewId;
        this.userId = userId;
    }

    public static ReviewCreatedEvent of(Long reviewId, Long userId) {
        return ReviewCreatedEvent.builder()
            .reviewId(reviewId)
            .userId(userId)
            .build();
    }

}
