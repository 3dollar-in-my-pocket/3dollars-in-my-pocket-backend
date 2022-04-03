package com.depromeet.threedollar.domain.rds.user.domain.storedelete.repository;

import java.time.LocalDate;

public interface StoreDeleteRequestStatisticsRepositoryCustom {

    long countBetweenDate(LocalDate startDate, LocalDate endDate);

}
