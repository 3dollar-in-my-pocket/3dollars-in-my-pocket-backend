package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StoreImageRepositoryCustom {

    @Nullable
    StoreImage findStoreImageById(Long storeImageId);

    List<StoreImage> findAllByStoreId(Long storeId);

}
