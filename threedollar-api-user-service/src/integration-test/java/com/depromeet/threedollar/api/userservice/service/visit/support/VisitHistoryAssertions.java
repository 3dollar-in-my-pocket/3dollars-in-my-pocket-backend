package com.depromeet.threedollar.api.userservice.service.visit.support;

import com.depromeet.threedollar.domain.rds.domain.TestAssertions;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestAssertions
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryAssertions {

    public static void assertVisitHistory(VisitHistory visitHistory, Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        assertAll(
            () -> assertThat(visitHistory.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(visitHistory.getUserId()).isEqualTo(userId),
            () -> assertThat(visitHistory.getType()).isEqualTo(type),
            () -> assertThat(visitHistory.getDateOfVisit()).isEqualTo(dateOfVisit)
        );
    }

}
