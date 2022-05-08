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
public class StoreDeleteRequestCreator {

    @Builder
    public static StoreDeleteRequest create(
        @NotNull Store store,
        @NotNull Long userId,
        @Nullable DeleteReasonType reasonType
    ) {
        return StoreDeleteRequest.builder()
            .store(store)
            .userId(userId)
            .reason(Optional.ofNullable(reasonType).orElse(DeleteReasonType.NOSTORE))
            .build();
    }

}
