package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics;

import com.depromeet.threedollar.domain.rds.core.support.OrderByNull;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.MenuStatisticsProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.QMenuStatisticsProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QMenu.menu;
import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QStore.store;

@RequiredArgsConstructor
public class MenuStaticsRepositoryCustomImpl implements MenuStaticsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuStatisticsProjection> countMenus() {
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
