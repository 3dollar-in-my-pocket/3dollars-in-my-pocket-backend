package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import static com.depromeet.threedollar.domain.rds.user.domain.store.QStoreImage.storeImage;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreImageRepositoryCustomImpl implements StoreImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Nullable
    @Override
    public StoreImage findStoreImageById(Long storeImageId) {
        return queryFactory.selectFrom(storeImage)
            .where(
                storeImage.id.eq(storeImageId),
                storeImage.status.eq(StoreImageStatus.ACTIVE)
            ).fetchOne();
    }

    @Override
    public List<StoreImage> findAllByStoreId(Long storeId) {
        return queryFactory.selectFrom(storeImage)
            .where(
                storeImage.store.id.eq(storeId),
                storeImage.status.eq(StoreImageStatus.ACTIVE)
            ).fetch();
    }

}
