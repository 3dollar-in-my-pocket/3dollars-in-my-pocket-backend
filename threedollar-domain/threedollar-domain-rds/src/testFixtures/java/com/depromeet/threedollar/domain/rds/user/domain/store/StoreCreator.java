package com.depromeet.threedollar.domain.rds.user.domain.store;

import java.util.Optional;

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
        return Store.builder()
            .userId(userId)
            .name(storeName)
            .type(Optional.ofNullable(storeType).orElse(StoreType.STORE))
            .latitude(Optional.ofNullable(latitude).orElse(34.0))
            .longitude(Optional.ofNullable(longitude).orElse(126.0))
            .rating(Optional.ofNullable(rating).orElse(0.0))
            .status(Optional.ofNullable(status).orElse(StoreStatus.ACTIVE))
            .build();
    }

}
