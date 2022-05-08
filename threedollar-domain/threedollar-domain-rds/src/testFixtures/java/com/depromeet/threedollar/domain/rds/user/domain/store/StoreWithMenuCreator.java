package com.depromeet.threedollar.domain.rds.user.domain.store;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.Builder;

@TestFixture
public class StoreWithMenuCreator {

    @Builder
    public static Store create(
        @NotNull Long userId,
        @NotNull String storeName,
        StoreType storeType,
        Double latitude,
        Double longitude,
        Double rating,
        StoreStatus status
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
