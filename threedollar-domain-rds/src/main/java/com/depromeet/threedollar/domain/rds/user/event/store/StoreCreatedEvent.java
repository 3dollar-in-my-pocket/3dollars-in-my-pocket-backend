package com.depromeet.threedollar.domain.rds.user.event.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCreatedEvent {

    private final Long storeId;

    private final Long userId;

    public static StoreCreatedEvent of(Long storeId, Long userId) {
        return new StoreCreatedEvent(storeId, userId);
    }

}
