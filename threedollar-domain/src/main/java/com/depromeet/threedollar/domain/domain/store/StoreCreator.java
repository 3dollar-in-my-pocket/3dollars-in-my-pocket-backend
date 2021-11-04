package com.depromeet.threedollar.domain.domain.store;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCreator {

    public static Store create(Long userId, String storeName) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(33.0)
            .longitude(124.0)
            .build();
    }

    public static Store create(Long userId, String storeName, double latitude, double longitude ) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

    public static Store create(Long userId, String storeName, StoreType storeType, double latitude, double longitude) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

    public static Store create(Long userId, String storeName, double latitude, double longitude, double rating) {
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(StoreType.STORE)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .build();
    }

}
