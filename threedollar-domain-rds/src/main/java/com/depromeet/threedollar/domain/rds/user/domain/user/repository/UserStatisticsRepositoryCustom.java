package com.depromeet.threedollar.domain.rds.user.domain.user.repository;

import java.time.LocalDate;

public interface UserStatisticsRepositoryCustom {

    long findUsersCount();

    long findUsersCountBetweenDate(LocalDate startDate, LocalDate endDate);

}
