package com.depromeet.threedollar.domain.rds.user.domain.review;

import com.depromeet.threedollar.domain.rds.user.domain.review.repository.ReviewRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.review.repository.ReviewStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom, ReviewStatisticsRepositoryCustom {

}
