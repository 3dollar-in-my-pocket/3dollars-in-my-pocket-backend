package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import static com.depromeet.threedollar.domain.rds.user.domain.store.QStoreImage.storeImage;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageStatus;
import com.depromeet.threedollar.domain.rds.user.domain.store.projection.QStoreImageProjection;
import com.depromeet.threedollar.domain.rds.user.domain.store.projection.StoreImageProjection;
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
    public List<StoreImageProjection> findAllByStoreId(Long storeId) {
        return queryFactory.select(new QStoreImageProjection(
                storeImage.id,
                storeImage.storeId,
                storeImage.userId,
                storeImage.url,
                storeImage.status,
                storeImage.createdAt,
                storeImage.updatedAt
            ))
            .from(storeImage)
            .where(
                storeImage.storeId.eq(storeId),
                storeImage.status.eq(StoreImageStatus.ACTIVE)
            ).fetch();
    }

}
