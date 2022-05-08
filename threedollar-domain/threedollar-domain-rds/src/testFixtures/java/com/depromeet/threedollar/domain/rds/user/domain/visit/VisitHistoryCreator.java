package com.depromeet.threedollar.domain.rds.user.domain.visit;

import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class VisitHistoryCreator {

    @Builder
    public static VisitHistory create(
        @NotNull Store store,
        @NotNull Long userId,
        @NotNull VisitType type,
        @NotNull LocalDate dateOfVisit
    ) {
        return VisitHistory.builder()
            .store(store)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build();
    }

}
