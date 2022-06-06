package com.depromeet.threedollar.domain.rds.event.userservice.review;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewChangedEvent {

    private final Long storeId;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewChangedEvent(Long storeId) {
        this.storeId = storeId;
    }

    public static ReviewChangedEvent of(Long storeId) {
        return ReviewChangedEvent.builder()
            .storeId(storeId)
            .build();
    }

}
