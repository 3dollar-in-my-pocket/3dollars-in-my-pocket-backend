package com.depromeet.threedollar.domain.rds.domain.userservice.store.repository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StoreRepositoryCustom {

    @Nullable
    Store findStoreById(Long storeId);

    @Nullable
    Store findStoreByIdFetchJoinMenu(Long storeId);

    List<Store> findAllByIds(List<Long> storeIds);

    long countByUserId(Long userId);

    List<Store> findAllUsingCursor(@Nullable Long lastStoreId, int size);

    List<StoreWithMenuProjection> findAllByUserIdUsingCursor(Long userId, @Nullable Long lastStoreId, int size);

    List<StoreWithMenuProjection> findStoresByLocationLessThanDistance(double latitude, double longitude, double distance);

    boolean existsStoreAroundInDistance(double latitude, double longitude, double distance);

}
