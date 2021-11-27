package com.depromeet.threedollar.domain.event.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDeletedEvent {

    private final Long storeId;

    private final Long userId;

    public static StoreDeletedEvent of(Long storeId, Long userId) {
        return new StoreDeletedEvent(storeId, userId);
    }

}
