package com.depromeet.threedollar.domain.user.event.review;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewChangedEvent {

    private final Long storeId;

    public static ReviewChangedEvent of(Long storeId) {
        return new ReviewChangedEvent(storeId);
    }

}
