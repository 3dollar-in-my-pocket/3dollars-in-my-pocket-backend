package com.depromeet.threedollar.domain.domain.storedelete.repository;

import com.depromeet.threedollar.domain.domain.store.StoreStatus;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequest;
import com.depromeet.threedollar.domain.domain.storedelete.repository.projection.QReportedStoreProjection;
import com.depromeet.threedollar.domain.domain.storedelete.repository.projection.ReportedStoreProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.domain.store.QStore.store;
import static com.depromeet.threedollar.domain.domain.storedelete.QStoreDeleteRequest.storeDeleteRequest;

@RequiredArgsConstructor
public class StoreDeleteRequestRepositoryCustomImpl implements StoreDeleteRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public StoreDeleteRequest findByStoreIdAndUserId(Long storeId, Long userId) {
        return queryFactory.selectFrom(storeDeleteRequest)
            .where(
                storeDeleteRequest.storeId.eq(storeId),
                storeDeleteRequest.userId.eq(userId)
            ).fetchOne();
    }

    @Override
    public List<StoreDeleteRequest> findAllByStoreId(Long storeId) {
        return queryFactory.selectFrom(storeDeleteRequest)
            .where(
                storeDeleteRequest.storeId.eq(storeId)
            ).fetch();
    }

    @Override
    public List<ReportedStoreProjection> findStoreHasDeleteRequestMoreThanCnt(int minCount) {
        return queryFactory.select(new QReportedStoreProjection(
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
            .from(storeDeleteRequest)
            .innerJoin(store).on(storeDeleteRequest.storeId.eq(store.id))
            .where(
                store.status.eq(StoreStatus.ACTIVE)
            )
            .groupBy(store.id)
            .having(store.id.count().goe(minCount))
            .orderBy(store.id.count().desc())
            .fetch();
    }

}
