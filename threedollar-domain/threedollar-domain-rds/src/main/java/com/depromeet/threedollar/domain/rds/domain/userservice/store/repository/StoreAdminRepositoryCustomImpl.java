package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import static com.depromeet.threedollar.domain.rds.user.domain.store.QStore.store;
import static com.depromeet.threedollar.domain.rds.user.domain.store.QStoreDeleteRequest.storeDeleteRequest;

import java.util.List;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithReportedCountProjection;
import com.depromeet.threedollar.domain.rds.user.domain.store.projection.QStoreWithReportedCountProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreAdminRepositoryCustomImpl implements StoreAdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreWithReportedCountProjection> findStoresByMoreThanReportCntWithPagination(int cnt, long offset, int size) {
        return queryFactory.select(new QStoreWithReportedCountProjection(
                store.id,
                store.name,
                store.location.latitude,
                store.location.longitude,
                store.type,
                store.rating,
                store.createdAt,
                store.updatedAt,
                storeDeleteRequest.id.count()
            ))
            .from(store)
            .innerJoin(storeDeleteRequest)
            .on(store.id.eq(storeDeleteRequest.store.id))
            .where(
                store.status.eq(StoreStatus.ACTIVE)
            )
            .groupBy(store.id)
            .having(store.id.count().goe(cnt))
            .orderBy(store.id.count().desc())
            .limit(size)
            .offset(offset * size)
            .fetch();
    }

}
