package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.store.Store;

public interface StoreRepositoryCustom {

    @Nullable
    Store findStoreById(Long storeId);

    @Nullable
    Store findStoreByIdFetchJoinMenu(Long storeId);

    List<Store> findAllByIds(List<Long> storeIds);

    long countByUserId(Long userId);

    List<Store> findAllUsingCursor(Long lastStoreId, int size);

    List<Store> findAllByUserIdUsingCursor(Long userId, Long lastStoreId, int size);

    List<Store> findStoresByLocationLessThanDistance(double latitude, double longitude, double distance);

    boolean existsStoreAroundInDistance(double latitude, double longitude, double distance);

}
