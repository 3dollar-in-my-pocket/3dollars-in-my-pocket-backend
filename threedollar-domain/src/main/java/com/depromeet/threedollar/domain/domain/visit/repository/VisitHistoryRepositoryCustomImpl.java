package com.depromeet.threedollar.domain.domain.visit.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.domain.visit.QVisitHistory.visitHistory;

@RequiredArgsConstructor
public class VisitHistoryRepositoryCustomImpl implements VisitHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit) {
        return queryFactory.selectOne()
            .from(visitHistory)
            .where(
                visitHistory.storeId.eq(storeId),
                visitHistory.userId.eq(userId),
                visitHistory.dateOfVisit.eq(dateOfVisit)
            ).fetchFirst() != null;
    }

}
