package com.depromeet.threedollar.domain.rds.domain.userservice.review;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.repository.ReviewRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.repository.statistics.ReviewStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom, ReviewStatisticsRepositoryCustom {

}
