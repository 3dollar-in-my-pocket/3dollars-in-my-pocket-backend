package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import static com.depromeet.threedollar.domain.rds.core.constants.RDBPackageConstants.PERSISTENCE_LOCK_TIMEOUT;
import static com.depromeet.threedollar.domain.rds.domain.userservice.store.QStoreDeleteRequest.storeDeleteRequest;

import java.util.List;

import javax.persistence.LockModeType;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreDeleteRequestRepositoryCustomImpl implements StoreDeleteRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findAllUserIdByStoreIdWithLock(Long storeId) {
        return queryFactory.select(storeDeleteRequest.userId)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setHint(PERSISTENCE_LOCK_TIMEOUT, 3000)
            .from(storeDeleteRequest)
            .where(
                storeDeleteRequest.store.id.eq(storeId)
            ).fetch();
    }

    @Override
    public long countsByUserId(Long userId) {
        return queryFactory.select(storeDeleteRequest.id)
            .from(storeDeleteRequest)
            .where(
                storeDeleteRequest.userId.eq(userId)
            ).fetchCount();
    }

}
