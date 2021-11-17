package com.depromeet.threedollar.domain.domain.review.repository;

import java.time.LocalDate;

public interface ReviewStaticsRepositoryCustom {

    long findActiveReviewsCounts();

    long findReviewsCountBetweenDate(LocalDate startDate, LocalDate endDate);

}
