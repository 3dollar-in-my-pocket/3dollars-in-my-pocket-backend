package com.depromeet.threedollar.domain.domain.visit.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.domain.visit.QVisitHistory.visitHistory;

@RequiredArgsConstructor
public class VisitHistoryStatisticsRepositoryCustomImpl implements VisitHistoryStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long findAllCounts() {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .fetchCount();
    }

    @Override
    public long findCountsBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .where(
                visitHistory.createdAt.goe(startDate.atStartOfDay()),
                visitHistory.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            ).fetchCount();
    }

}
