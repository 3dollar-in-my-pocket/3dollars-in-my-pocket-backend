package com.depromeet.threedollar.domain.rds.user.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.review.repository.ReviewRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.review.repository.ReviewStatisticsRepositoryCustom;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom, ReviewStatisticsRepositoryCustom {

}
