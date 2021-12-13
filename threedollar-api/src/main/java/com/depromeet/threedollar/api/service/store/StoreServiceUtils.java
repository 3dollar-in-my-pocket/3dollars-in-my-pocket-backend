package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRadiusDistance;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_STORE_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreServiceUtils {

    public static void validateExistsStore(StoreRepository storeRepository, Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException(String.format("해당하는 가게 (%s)는 존재하지 않습니다", storeId), NOT_FOUND_STORE_EXCEPTION);
        }
    }

    @NotNull
    public static Store findStoreById(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreById(storeId);
        if (store == null) {
            throw new NotFoundException(String.format("해당하는 가게 (%s)는 존재하지 않습니다", storeId), NOT_FOUND_STORE_EXCEPTION);
        }
        return store;
    }

    @NotNull
    static Store findStoreByIdFetchJoinMenu(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreByIdFetchJoinMenu(storeId);
        if (store == null) {
            throw new NotFoundException(String.format("해당하는 가게 (%s)는 존재하지 않습니다", storeId), NOT_FOUND_STORE_EXCEPTION);
        }
        return store;
    }

    static List<Store> findNearStoresFilterByCategory(StoreRepository storeRepository, double mapLatitude, double mapLongitude,
                                                      StoreRadiusDistance distance, @Nullable MenuCategoryType categoryType) {
        List<Store> nearStores = storeRepository.findStoresByLocationLessThanDistance(mapLatitude, mapLongitude, distance.getDistance());
        if (categoryType == null) {
            return nearStores;
        }
        return nearStores.stream()
            .filter(store -> store.hasMenuCategory(categoryType))
            .collect(Collectors.toList());
    }

}
