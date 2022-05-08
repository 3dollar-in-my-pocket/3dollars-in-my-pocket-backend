package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class StoreImageCreator {

    @Builder
    public static StoreImage create(
        @NotNull Store store,
        @NotNull Long userId,
        @NotNull String url
    ) {
        return StoreImage.builder()
            .store(store)
            .userId(userId)
            .url(url)
            .build();
    }

}
