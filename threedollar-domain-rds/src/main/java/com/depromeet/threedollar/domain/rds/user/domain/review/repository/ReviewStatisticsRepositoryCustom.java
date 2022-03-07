package com.depromeet.threedollar.domain.rds.user.domain.review.repository;

import java.time.LocalDate;

public interface ReviewStatisticsRepositoryCustom {

    long findActiveReviewsCounts();

    long findReviewsCountBetweenDate(LocalDate startDate, LocalDate endDate);

}
