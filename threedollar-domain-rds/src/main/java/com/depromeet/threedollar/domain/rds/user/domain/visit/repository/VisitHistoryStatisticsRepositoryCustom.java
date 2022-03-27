package com.depromeet.threedollar.domain.rds.user.domain.visit.repository;

import java.time.LocalDate;

public interface VisitHistoryStatisticsRepositoryCustom {

    long countAllVisitHistoriese();

    long countVisitHistoriesBetweenDate(LocalDate startDate, LocalDate endDate);

}
