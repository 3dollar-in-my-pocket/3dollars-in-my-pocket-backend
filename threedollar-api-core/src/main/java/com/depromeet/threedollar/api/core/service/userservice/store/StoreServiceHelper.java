package com.depromeet.threedollar.api.core.service.userservice.store;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E404_NOT_EXISTS_STORE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreServiceHelper {

    public static void validateExistsStore(StoreRepository storeRepository, Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException(String.format("해당하는 유저 가게(%s)는 존재하지 않습니다.", storeId), E404_NOT_EXISTS_STORE);
        }
    }

    @NotNull
    public static Store findStoreById(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreById(storeId);
        if (store == null) {
            throw new NotFoundException(String.format("해당하는 유저 가게(%s)는 존재하지 않습니다", storeId), E404_NOT_EXISTS_STORE);
        }
        return store;
    }

    @NotNull
    public static Store findStoreByIdFetchJoinMenu(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreByIdFetchJoinMenu(storeId);
        if (store == null) {
            throw new NotFoundException(String.format("해당하는 유저 가게(%s)는 존재하지 않습니다", storeId), E404_NOT_EXISTS_STORE);
        }
        return store;
    }

}
