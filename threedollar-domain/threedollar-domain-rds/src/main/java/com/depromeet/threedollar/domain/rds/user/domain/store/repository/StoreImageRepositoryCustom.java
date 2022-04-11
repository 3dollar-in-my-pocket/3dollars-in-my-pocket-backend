package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;

public interface StoreImageRepositoryCustom {

    @Nullable
    StoreImage findStoreImageById(Long storeImageId);

    List<StoreImage> findAllByStoreId(Long storeId);

}
