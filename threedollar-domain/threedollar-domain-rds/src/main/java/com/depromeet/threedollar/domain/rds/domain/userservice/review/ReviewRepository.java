package com.depromeet.threedollar.domain.rds.domain.userservice.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.repository.ReviewRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.repository.statistics.ReviewStatisticsRepositoryCustom;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom, ReviewStatisticsRepositoryCustom {

}
