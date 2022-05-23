package com.depromeet.threedollar.domain.rds.vendor.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.review.repository.ReviewRepositoryCustom;
import com.depromeet.threedollar.domain.rds.vendor.domain.review.repository.ReviewStatisticsRepositoryCustom;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom, ReviewStatisticsRepositoryCustom {

}
