package com.depromeet.threedollar.domain.rds.domain.userservice.user.repository;

import java.time.LocalDate;

public interface UserStatisticsRepositoryCustom {

    long countAllUsers();

    long countUsersBetweenDate(LocalDate startDate, LocalDate endDate);

}
