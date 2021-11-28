package com.depromeet.threedollar.domain.domain.store.repository;

import com.depromeet.threedollar.domain.config.querydsl.OrderByNull;
import com.depromeet.threedollar.domain.domain.store.StoreStatus;
import com.depromeet.threedollar.domain.domain.store.projection.MenuStatisticsProjection;
import com.depromeet.threedollar.domain.domain.store.projection.QMenuStatisticsProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.domain.store.QMenu.menu;
import static com.depromeet.threedollar.domain.domain.store.QStore.store;

@RequiredArgsConstructor
public class MenuStaticsRepositoryCustomImpl implements MenuStaticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuStatisticsProjection> countsGroupByMenu() {
        return queryFactory.select(new QMenuStatisticsProjection(menu.category, menu.id.count()))
            .from(menu)
            .innerJoin(menu.store, store)
            .where(
                store.status.eq(StoreStatus.ACTIVE)
            )
            .groupBy(menu.category)
            .orderBy(OrderByNull.DEFAULT)
            .fetch();
    }

}