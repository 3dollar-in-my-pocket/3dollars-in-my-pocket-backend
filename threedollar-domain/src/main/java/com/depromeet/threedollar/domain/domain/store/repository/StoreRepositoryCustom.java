package com.depromeet.threedollar.domain.domain.store.repository;

import com.depromeet.threedollar.domain.domain.store.Store;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StoreRepositoryCustom {

    @Nullable
    Store findStoreById(Long storeId);

    @Nullable
    Store findStoreByIdFetchJoinMenu(Long storeId);

    List<Store> findAllByIds(List<Long> storeIds);

    long findCountsByUserId(Long userId);

    @Deprecated
    long findActiveCountsByUserId(Long userId);

    List<Store> findAllWithScroll(Long lastStoreId, int size);

    List<Store> findAllByUserIdWithScroll(Long userId, Long lastStoreId, int size);

    @Deprecated
    List<Store> findAllActiveByUserIdWithScroll(Long userId, Long lastStoreId, int size);

    List<Store> findStoresByLocationLessThanDistance(double latitude, double longitude, double distance);

}
