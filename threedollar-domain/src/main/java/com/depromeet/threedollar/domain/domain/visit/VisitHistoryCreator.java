package com.depromeet.threedollar.domain.domain.visit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryCreator {

    public static VisitHistory create(Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        return VisitHistory.builder()
            .storeId(storeId)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build();
    }

}
