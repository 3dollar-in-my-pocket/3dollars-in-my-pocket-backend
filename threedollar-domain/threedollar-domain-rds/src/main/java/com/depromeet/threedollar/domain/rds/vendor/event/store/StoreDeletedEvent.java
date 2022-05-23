package com.depromeet.threedollar.domain.rds.vendor.event.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreDeletedEvent {

    private final Long storeId;

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreDeletedEvent(Long storeId, Long userId) {
        this.storeId = storeId;
        this.userId = userId;
    }

    public static StoreDeletedEvent of(Long storeId, Long userId) {
        return StoreDeletedEvent.builder()
            .storeId(storeId)
            .userId(userId)
            .build();
    }

}
