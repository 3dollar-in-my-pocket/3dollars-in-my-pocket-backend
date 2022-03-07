package com.depromeet.threedollar.domain.rds.user.domain.visit.repository;

import java.time.LocalDate;

public interface VisitHistoryStatisticsRepositoryCustom {

    long findAllCounts();

    long findCountsBetweenDate(LocalDate startDate, LocalDate endDate);

}
