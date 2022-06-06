package com.depromeet.threedollar.domain.rds.event.userservice.store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreCreatedEvent {

    private final Long storeId;

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreCreatedEvent(Long storeId, Long userId) {
        this.storeId = storeId;
        this.userId = userId;
    }

    public static StoreCreatedEvent of(Long storeId, Long userId) {
        return StoreCreatedEvent.builder()
            .storeId(storeId)
            .userId(userId)
            .build();
    }

}
