package com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository;

import java.time.LocalDate;

public interface VisitHistoryStatisticsRepositoryCustom {

    long countAllVisitHistoriese();

    long countVisitHistoriesBetweenDate(LocalDate startDate, LocalDate endDate);

}
