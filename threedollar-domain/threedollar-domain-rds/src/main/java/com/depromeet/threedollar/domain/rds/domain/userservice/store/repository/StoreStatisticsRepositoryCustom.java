package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import java.time.LocalDate;

public interface StoreStatisticsRepositoryCustom {

    long countAllActiveStores();

    long countActiveStoresBetweenDate(LocalDate startDate, LocalDate endDate);

    long countDeletedStoresBetweenDate(LocalDate startDate, LocalDate endDate);

    long countAllDeletedStores();

}
