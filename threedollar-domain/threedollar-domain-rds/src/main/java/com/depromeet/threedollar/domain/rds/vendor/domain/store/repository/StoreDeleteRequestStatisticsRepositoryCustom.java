package com.depromeet.threedollar.domain.rds.vendor.domain.store.repository;

import java.time.LocalDate;

public interface StoreDeleteRequestStatisticsRepositoryCustom {

    long countBetweenDate(LocalDate startDate, LocalDate endDate);

}
