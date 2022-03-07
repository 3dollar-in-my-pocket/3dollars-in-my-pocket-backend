package com.depromeet.threedollar.domain.rds.user.domain.store.repository;

import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StoreRepositoryCustom {

    @Nullable
    Store findStoreById(Long storeId);

    @Nullable
    Store findStoreByIdFetchJoinMenu(Long storeId);

    List<Store> findAllByIds(List<Long> storeIds);

    long findCountsByUserId(Long userId);

    List<Store> findAllUsingCursor(Long lastStoreId, int size);

    List<Store> findAllByUserIdUsingCursor(Long userId, Long lastStoreId, int size);

    List<Store> findStoresByLocationLessThanDistance(double latitude, double longitude, double distance);

    boolean existsStoreAroundInDistance(double latitude, double longitude, double distance);

}
