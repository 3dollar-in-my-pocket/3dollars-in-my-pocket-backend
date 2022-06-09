package com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository.statistics;

import java.time.LocalDate;

public interface VisitHistoryStatisticsRepositoryCustom {

    long countAllVisitHistories();

    long countVisitHistoriesBetweenDate(LocalDate startDate, LocalDate endDate);

}
