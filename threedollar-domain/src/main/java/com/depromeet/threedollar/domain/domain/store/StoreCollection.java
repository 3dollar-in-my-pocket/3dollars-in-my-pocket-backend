package com.depromeet.threedollar.domain.domain.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCollection {

    private Map<Long, Store> cachedStore;

    public static StoreCollection of(List<Store> stores) {
        return new StoreCollection(stores.stream()
            .collect(Collectors.toMap(Store::getId, store -> store))
        );
    }

    public Store getStore(Long storeId) {
        return cachedStore.get(storeId);
    }

}
