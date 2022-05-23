package com.depromeet.threedollar.domain.rds.vendor.domain.user.repository;

import java.time.LocalDate;

public interface UserStatisticsRepositoryCustom {

    long countAllUsers();

    long countUsersBetweenDate(LocalDate startDate, LocalDate endDate);

}
