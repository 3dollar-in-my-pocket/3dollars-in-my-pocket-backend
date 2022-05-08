package com.depromeet.threedollar.domain.rds.user.domain.store;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class StoreWithMenuCreator {

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
        Store store = StoreCreator.builder()
            .userId(userId)
            .storeName(storeName)
            .storeType(storeType)
            .latitude(latitude)
            .longitude(longitude)
            .rating(rating)
            .status(status)
            .build();
        store.addMenus(List.of(MenuCreator.builder()
            .store(store)
            .name("메뉴 이름")
            .price("메뉴 가격")
            .category(MenuCategoryType.BUNGEOPPANG)
            .build()
        ));
        return store;
    }

}
