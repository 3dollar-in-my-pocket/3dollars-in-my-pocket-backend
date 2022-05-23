package com.depromeet.threedollar.domain.rds.vendor.domain.store.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.projection.StoreImageProjection;

public interface StoreImageRepositoryCustom {

    @Nullable
    StoreImage findStoreImageById(Long storeImageId);

    List<StoreImageProjection> findAllByStoreId(Long storeId);

}
