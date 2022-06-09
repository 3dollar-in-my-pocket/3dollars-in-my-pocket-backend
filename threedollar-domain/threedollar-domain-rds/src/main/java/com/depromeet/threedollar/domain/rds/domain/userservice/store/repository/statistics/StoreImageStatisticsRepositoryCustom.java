package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics;

import java.time.LocalDate;

public interface StoreImageStatisticsRepositoryCustom {

    long countBetweenDate(LocalDate startDate, LocalDate endDate);

}
