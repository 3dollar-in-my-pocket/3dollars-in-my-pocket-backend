package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.Builder;

@TestFixture
public class MenuCreator {

    @Builder
    public static Menu create(
        @NotNull Store store,
        @NotNull String name,
        @NotNull String price,
        @NotNull MenuCategoryType category
    ) {
        return Menu.builder()
            .store(store)
            .name(name)
            .price(price)
            .category(category)
            .build();
    }

}
