package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.common.exception.notfound.NotFoundStoreException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreServiceUtils {

    static void validateExistsStore(StoreRepository storeRepository, Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundStoreException(String.format("해당하는 가게 (%s)는 존재하지 않습니다", storeId));
        }
    }

    public static Store findStoreById(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreById(storeId);
        if (store == null) {
            throw new NotFoundStoreException(String.format("해당하는 가게 (%s)는 존재하지 않습니다", storeId));
        }
        return store;
    }

    static Store findStoreByIdFetchJoinMenu(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreByIdFetchJoinMenu(storeId);
        if (store == null) {
            throw new NotFoundStoreException(String.format("해당하는 가게 (%s)는 존재하지 않습니다", storeId));
        }
        return store;
    }

}
