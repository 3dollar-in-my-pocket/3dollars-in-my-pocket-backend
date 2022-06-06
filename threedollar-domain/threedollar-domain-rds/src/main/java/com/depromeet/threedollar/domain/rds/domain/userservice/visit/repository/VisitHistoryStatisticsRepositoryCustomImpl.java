package com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository;

import static com.depromeet.threedollar.domain.rds.user.domain.visit.QVisitHistory.visitHistory;

import java.time.LocalDate;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisitHistoryStatisticsRepositoryCustomImpl implements VisitHistoryStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countAllVisitHistoriese() {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .fetchCount();
    }

    @Override
    public long countVisitHistoriesBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .where(
                visitHistory.createdAt.goe(startDate.atStartOfDay()),
                visitHistory.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            ).fetchCount();
    }

}
