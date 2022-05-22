package com.depromeet.threedollar.domain.rds.user.domain.visit.repository;

import static com.depromeet.threedollar.domain.rds.common.support.QuerydslSupport.predicate;
import static com.depromeet.threedollar.domain.rds.user.domain.store.QMenu.menu;
import static com.depromeet.threedollar.domain.rds.user.domain.store.QStore.store;
import static com.depromeet.threedollar.domain.rds.user.domain.user.QUser.user;
import static com.depromeet.threedollar.domain.rds.user.domain.visit.QVisitHistory.visitHistory;

import java.time.LocalDate;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.QVisitHistoryCountProjection;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.QVisitHistoryWithUserProjection;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryCountProjection;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryWithUserProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisitHistoryRepositoryCustomImpl implements VisitHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit) {
        return queryFactory.selectOne()
            .from(visitHistory)
            .where(
                visitHistory.store.id.eq(storeId),
                visitHistory.dateOfVisit.eq(dateOfVisit),
                visitHistory.userId.eq(userId)
            ).fetchFirst() != null;
    }

    @Override
    public List<VisitHistoryWithUserProjection> findAllVisitWithUserByStoreIdAfterDate(Long storeId, LocalDate startDate) {
        return queryFactory.select(new QVisitHistoryWithUserProjection(
                visitHistory,
                user
            ))
            .from(visitHistory)
            .leftJoin(user).on(visitHistory.userId.eq(user.id))
            .where(
                visitHistory.store.id.eq(storeId),
                visitHistory.dateOfVisit.goe(startDate)
            )
            .orderBy(visitHistory.id.desc())
            .fetch();
    }

    @Override
    public List<VisitHistory> findAllByUserIdUsingCursor(Long userId, @Nullable Long lastHistoryId, int size) {
        List<Long> visitHistoriesIds = queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .where(
                visitHistory.userId.eq(userId),
                predicate(lastHistoryId != null, () -> visitHistory.id.lt(lastHistoryId))
            )
            .orderBy(visitHistory.id.desc())
            .limit(size)
            .fetch();

        return queryFactory.selectFrom(visitHistory).distinct()
            .innerJoin(visitHistory.store, store).fetchJoin()
            .innerJoin(store.menus, menu).fetchJoin()
            .where(
                visitHistory.id.in(visitHistoriesIds)
            )
            .orderBy(visitHistory.id.desc())
            .fetch();
    }

    @Override
    public List<VisitHistoryCountProjection> countGroupingByStoreId(List<Long> storeIds, LocalDate startDate) {
        return queryFactory.select(new QVisitHistoryCountProjection(visitHistory.store.id, visitHistory.type, visitHistory.id.count()))
            .from(visitHistory)
            .where(
                visitHistory.store.id.in(storeIds),
                visitHistory.dateOfVisit.goe(startDate)
            )
            .groupBy(visitHistory.store.id, visitHistory.type)
            .fetch();
    }

    @Override
    public long countByUserIdAndMenuCategoryType(Long userId, @Nullable UserMenuCategoryType menuCategoryType) {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .innerJoin(visitHistory.store, store).fetchJoin()
            .innerJoin(store.menus, menu).fetchJoin()
            .where(
                visitHistory.userId.eq(userId),
                predicate(menuCategoryType != null, () -> menu.category.eq(menuCategoryType))
            ).fetchCount();
    }

    @Override
    public long countByUserIdAndVisitType(Long userId, VisitType visitType) {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .where(
                visitHistory.userId.eq(userId),
                visitHistory.type.eq(visitType)
            ).fetchCount();
    }

}
