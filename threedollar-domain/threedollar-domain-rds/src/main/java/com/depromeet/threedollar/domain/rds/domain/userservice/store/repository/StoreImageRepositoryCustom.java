package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreImageProjection;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StoreImageRepositoryCustom {

    @Nullable
    StoreImage findStoreImageById(Long storeImageId);

    List<StoreImageProjection> findAllByStoreId(Long storeId);

}
