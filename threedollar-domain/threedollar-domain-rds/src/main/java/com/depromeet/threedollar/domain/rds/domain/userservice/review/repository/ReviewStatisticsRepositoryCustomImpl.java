package com.depromeet.threedollar.domain.rds.domain.userservice.review.repository;

import static com.depromeet.threedollar.domain.rds.domain.userservice.review.QReview.review;

import java.time.LocalDate;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewStatisticsRepositoryCustomImpl implements ReviewStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countActiveReviews() {
        return queryFactory.select(review.id)
            .from(review)
            .where(
                review.status.eq(ReviewStatus.POSTED)
            ).fetchCount();
    }

    @Override
    public long countActiveReviewsBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(review.id)
            .from(review)
            .where(
                review.status.eq(ReviewStatus.POSTED),
                review.createdAt.goe(startDate.atStartOfDay()),
                review.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            ).fetchCount();
    }

}
