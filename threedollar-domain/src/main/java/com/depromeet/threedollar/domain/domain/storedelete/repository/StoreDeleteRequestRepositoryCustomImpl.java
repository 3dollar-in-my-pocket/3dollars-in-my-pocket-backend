package com.depromeet.threedollar.domain.domain.storedelete.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.LockModeType;
import java.util.List;

import static com.depromeet.threedollar.domain.domain.storedelete.QStoreDeleteRequest.storeDeleteRequest;

@RequiredArgsConstructor
public class StoreDeleteRequestRepositoryCustomImpl implements StoreDeleteRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findAllUserIdByStoreIdWithLock(Long storeId) {
        return queryFactory.select(storeDeleteRequest.userId)
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .setHint("javax.persistence.lock.timeout", 2000)
            .from(storeDeleteRequest)
            .where(
                storeDeleteRequest.store.id.eq(storeId)
            ).fetch();
    }

    @Override
    public long findCountsByUserId(Long userId) {
        return queryFactory.select(storeDeleteRequest.id)
            .from(storeDeleteRequest)
            .where(
                storeDeleteRequest.userId.eq(userId)
            ).fetchCount();
    }

}
