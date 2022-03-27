package com.depromeet.threedollar.domain.rds.user.domain.review.repository;

import java.time.LocalDate;

public interface ReviewStatisticsRepositoryCustom {

    long countActiveReviews();

    long countActiveReviewsBetweenDate(LocalDate startDate, LocalDate endDate);

}
