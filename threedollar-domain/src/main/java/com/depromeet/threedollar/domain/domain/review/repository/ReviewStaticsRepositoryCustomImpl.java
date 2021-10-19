package com.depromeet.threedollar.domain.domain.review.repository;

import com.depromeet.threedollar.domain.domain.review.ReviewStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.domain.review.QReview.review;

@RequiredArgsConstructor
public class ReviewStaticsRepositoryCustomImpl implements ReviewStaticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long findReviewsCount() {
        return queryFactory.select(review.id)
            .from(review)
            .where(
                review.status.eq(ReviewStatus.POSTED)
            ).fetchCount();
    }

    @Override
    public long findReviewsCountBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(review.id)
            .from(review)
            .where(
                review.status.eq(ReviewStatus.POSTED),
                review.createdAt.goe(startDate.atStartOfDay()),
                review.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            ).fetchCount();
    }

}
