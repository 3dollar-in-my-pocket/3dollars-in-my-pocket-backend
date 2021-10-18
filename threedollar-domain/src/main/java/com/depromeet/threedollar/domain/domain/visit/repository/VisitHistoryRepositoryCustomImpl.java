package com.depromeet.threedollar.domain.domain.visit.repository;

import com.depromeet.threedollar.domain.domain.visit.projection.QVisitHistoryWithUserProjection;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.domain.domain.visit.QVisitHistory.visitHistory;
import static com.depromeet.threedollar.domain.domain.user.QUser.user;

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

    @Override
    public List<VisitHistoryWithUserProjection> findVisitWithUserByStoreIdBetweenDate(Long storeId, LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(new QVisitHistoryWithUserProjection(
            visitHistory.id,
            visitHistory.storeId,
            visitHistory.type,
            visitHistory.dateOfVisit,
            visitHistory.createdAt,
            visitHistory.updatedAt,
            visitHistory.userId,
            user.name
        ))
            .from(visitHistory)
            .leftJoin(user).on(visitHistory.userId.eq(user.id))
            .where(
                visitHistory.storeId.eq(storeId),
                visitHistory.dateOfVisit.goe(startDate),
                visitHistory.dateOfVisit.loe(endDate)
            ).fetch();
    }

}
