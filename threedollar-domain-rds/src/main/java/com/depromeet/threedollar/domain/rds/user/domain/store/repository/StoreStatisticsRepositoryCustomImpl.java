package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.domain.rds.user.domain.store.QMenu.menu;
import static com.depromeet.threedollar.domain.rds.user.domain.store.QStore.store;

@RequiredArgsConstructor
public class StoreStatisticsRepositoryCustomImpl implements StoreStatisticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countAllActiveStores() {
        return queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.status.eq(StoreStatus.ACTIVE)
            ).fetchCount();
    }

    @Override
    public long countActiveStoresBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.status.eq(StoreStatus.ACTIVE),
                store.createdAt.goe(startDate.atStartOfDay()),
                store.createdAt.lt(endDate.atStartOfDay().plusDays(1))
            ).fetchCount();
    }

    @Override
    public long countDeletedStoresBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(store.id).distinct()
            .from(store)
            .innerJoin(menu).on(menu.store.id.eq(store.id))
            .where(
                store.status.eq(StoreStatus.DELETED),
                store.updatedAt.goe(startDate.atStartOfDay()),
                store.updatedAt.lt(endDate.atStartOfDay().plusDays(1))
            ).fetchCount();
    }

}
