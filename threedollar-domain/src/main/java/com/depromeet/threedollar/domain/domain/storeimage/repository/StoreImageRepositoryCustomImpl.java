package com.depromeet.threedollar.domain.domain.storeimage.repository;

import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.depromeet.threedollar.domain.domain.storeimage.QStoreImage.storeImage;

@RequiredArgsConstructor
public class StoreImageRepositoryCustomImpl implements StoreImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
