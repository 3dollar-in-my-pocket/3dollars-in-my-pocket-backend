package com.depromeet.threedollar.api.vendor.service.visit.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;

import com.depromeet.threedollar.domain.rds.vendor.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.vendor.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.vendor.domain.visit.VisitType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
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
