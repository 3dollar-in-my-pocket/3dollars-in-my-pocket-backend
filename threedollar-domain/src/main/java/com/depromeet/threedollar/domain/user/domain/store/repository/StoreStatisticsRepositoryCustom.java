package com.depromeet.threedollar.domain.user.domain.store.repository;

import java.time.LocalDate;

public interface StoreStatisticsRepositoryCustom {

    long findActiveStoresCounts();

    long findActiveStoresCountsBetweenDate(LocalDate startDate, LocalDate endDate);

    long findDeletedStoresCountsByDate(LocalDate startDate, LocalDate endDate);

}
