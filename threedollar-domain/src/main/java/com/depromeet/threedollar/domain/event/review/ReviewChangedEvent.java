package com.depromeet.threedollar.domain.event.review;

import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewChangedEvent {

    private final Store store;

    public static ReviewChangedEvent of(Store store) {
        return new ReviewChangedEvent(store);
    }

}
