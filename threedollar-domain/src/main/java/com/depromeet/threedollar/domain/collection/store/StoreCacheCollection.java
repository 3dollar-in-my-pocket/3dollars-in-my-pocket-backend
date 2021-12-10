package com.depromeet.threedollar.domain.collection.store;

import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCacheCollection {

    private final Map<Long, Store> cachedStore;

    public static StoreCacheCollection of(List<Store> stores) {
        return new StoreCacheCollection(stores.stream()
            .collect(Collectors.toMap(Store::getId, store -> store)));
    }

    public Store getStore(Long storeId) {
        return cachedStore.get(storeId);
    }

}
