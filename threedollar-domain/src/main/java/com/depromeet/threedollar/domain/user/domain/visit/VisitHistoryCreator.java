package com.depromeet.threedollar.domain.user.domain.visit;

import com.depromeet.threedollar.common.docs.ObjectMother;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@ObjectMother
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