package com.depromeet.threedollar.domain.domain.visit.repository;

import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.depromeet.threedollar.domain.domain.visit.projection.QVisitHistoryCountProjection;
import com.depromeet.threedollar.domain.domain.visit.projection.QVisitHistoryWithUserProjection;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryCountProjection;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.domain.domain.store.QMenu.menu;
import static com.depromeet.threedollar.domain.domain.store.QStore.store;
import static com.depromeet.threedollar.domain.domain.user.QUser.user;
import static com.depromeet.threedollar.domain.domain.visit.QVisitHistory.visitHistory;

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
            visitHistory.id,
            visitHistory.store.id,
            visitHistory.type,
            visitHistory.dateOfVisit,
            visitHistory.createdAt,
            visitHistory.updatedAt,
            visitHistory.userId,
            user.name,
            user.socialInfo.socialType
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
    public List<VisitHistory> findAllByUserIdWithScroll(Long userId, Long lastHistoryId, int size) {
        List<Long> visitHistoriesIds = queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .where(
                visitHistory.userId.eq(userId),
                lessThanId(lastHistoryId)
            ).limit(size)
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

    private BooleanExpression lessThanId(Long lastHistoryId) {
        if (lastHistoryId == null) {
            return null;
        }
        return visitHistory.id.lt(lastHistoryId);
    }

    @Override
    public List<VisitHistoryCountProjection> findCountsByStoreIdWithGroup(List<Long> storeIds, LocalDate startDate) {
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
    public long findCountsByUserIdAndCategory(Long userId, @Nullable MenuCategoryType menuCategoryType) {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .innerJoin(visitHistory.store, store).fetchJoin()
            .innerJoin(store.menus, menu).fetchJoin()
            .where(
                visitHistory.userId.eq(userId),
                eqMenuCategory(menuCategoryType)
            ).fetchCount();
    }

    private BooleanExpression eqMenuCategory(@Nullable MenuCategoryType menuCategoryType) {
        if (menuCategoryType == null) {
            return null;
        }
        return menu.category.eq(menuCategoryType);
    }

    @Override
    public long findCountsByUserIdAndVisitType(Long userId, VisitType visitType) {
        return queryFactory.select(visitHistory.id)
            .from(visitHistory)
            .where(
                visitHistory.userId.eq(userId),
                visitHistory.type.eq(visitType)
            ).fetchCount();
    }

}
