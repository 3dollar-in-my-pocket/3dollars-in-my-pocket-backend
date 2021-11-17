package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryCreator {

    public static VisitHistory create(Store store, Long userId, VisitType type, LocalDate dateOfVisit) {
        return VisitHistory.builder()
            .store(store)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build();
    }

}
