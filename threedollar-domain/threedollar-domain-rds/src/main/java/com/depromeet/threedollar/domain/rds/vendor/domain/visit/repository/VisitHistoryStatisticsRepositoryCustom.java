package com.depromeet.threedollar.domain.rds.vendor.domain.visit.repository;

import java.time.LocalDate;

public interface VisitHistoryStatisticsRepositoryCustom {

    long countAllVisitHistoriese();

    long countVisitHistoriesBetweenDate(LocalDate startDate, LocalDate endDate);

}
