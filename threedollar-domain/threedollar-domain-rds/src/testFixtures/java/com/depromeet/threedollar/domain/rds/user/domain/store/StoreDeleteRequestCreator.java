package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class StoreDeleteRequestCreator {

    @Builder
    public static StoreDeleteRequest create(
        @NotNull Store store,
        @NotNull Long userId,
        @NotNull DeleteReasonType reasonType
    ) {
        return StoreDeleteRequest.builder()
            .store(store)
            .userId(userId)
            .reason(reasonType)
            .build();
    }

}
