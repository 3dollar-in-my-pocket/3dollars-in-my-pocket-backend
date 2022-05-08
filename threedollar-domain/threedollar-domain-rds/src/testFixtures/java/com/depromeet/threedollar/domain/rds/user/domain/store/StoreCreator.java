package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class StoreCreator {

    @Builder
    public static Store create(
        @NotNull Long userId,
        @NotNull String storeName,
        @Nullable StoreType storeType,
        @Nullable Double latitude,
        @Nullable Double longitude,
        @Nullable Double rating,
        @Nullable StoreStatus status
    ) {
        if (latitude == null) {
            latitude = 34.0;
        }
        if (longitude == null) {
            longitude = 126.0;
        }
        if (storeType == null) {
            storeType = StoreType.STORE;
        }
        if (rating == null) {
            rating = 0.0;
        }
        if (status == null) {
            status = StoreStatus.ACTIVE;
        }
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .status(status)
            .build();
    }

}
