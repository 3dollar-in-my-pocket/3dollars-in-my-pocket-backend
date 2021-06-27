package com.depromeet.threedollar.domain.domain.review;

import com.depromeet.threedollar.domain.domain.review.repository.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

}
