package com.depromeet.threedollar.domain.user.collection.store;

import com.depromeet.threedollar.domain.user.domain.store.Store;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDictionary {

    private final Map<Long, Store> dictionary;

    public static StoreDictionary of(List<Store> stores) {
        return new StoreDictionary(stores.stream()
            .collect(Collectors.toMap(Store::getId, store -> store)));
    }

    @NotNull
    public Store getStore(Long storeId) {
        return dictionary.get(storeId);
    }

}
