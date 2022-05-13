package com.depromeet.threedollar.api.user.service.store;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOTFOUND_STORE;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.user.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.redis.domain.user.store.CachedAroundStoreRepository;
import com.depromeet.threedollar.domain.redis.domain.user.store.CachedAroundStoreValue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreServiceUtils {

    public static void validateExistsStore(StoreRepository storeRepository, Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException(String.format("해당하는 유저 가게(%s)는 존재하지 않습니다.", storeId), NOTFOUND_STORE);
        }
    }

    @NotNull
    public static Store findStoreById(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreById(storeId);
        if (store == null) {
            throw new NotFoundException(String.format("해당하는 유저 가게(%s)는 존재하지 않습니다", storeId), NOTFOUND_STORE);
        }
        return store;
    }

    @NotNull
    static Store findStoreByIdFetchJoinMenu(StoreRepository storeRepository, Long storeId) {
        Store store = storeRepository.findStoreByIdFetchJoinMenu(storeId);
        if (store == null) {
            throw new NotFoundException(String.format("해당하는 유저 가게(%s)는 존재하지 않습니다", storeId), NOTFOUND_STORE);
        }
        return store;
    }

    static List<StoreInfoResponse> findNearStoresFilterByCategory(StoreRepository storeRepository, CachedAroundStoreRepository cachedAroundStoreRepository,
                                                                  double mapLatitude, double mapLongitude, double distance, @Nullable MenuCategoryType categoryType) {
        List<CachedAroundStoreValue> cachedStores = cachedAroundStoreRepository.get(mapLatitude, mapLongitude, distance);
        if (cachedStores != null) {
            return getNearStoresFilerByCategory(cachedStores, categoryType);
        }

        List<Store> nearStores = storeRepository.findStoresByLocationLessThanDistance(mapLatitude, mapLongitude, distance);

        saveAroundStoresInCached(cachedAroundStoreRepository, nearStores, mapLatitude, mapLongitude, distance);

        if (categoryType == null) {
            return nearStores.stream()
                .map(StoreInfoResponse::of)
                .collect(Collectors.toList());
        }
        return nearStores.stream()
            .filter(store -> store.hasMenuCategory(categoryType))
            .map(StoreInfoResponse::of)
            .collect(Collectors.toList());
    }

    private static List<StoreInfoResponse> getNearStoresFilerByCategory(List<CachedAroundStoreValue> cachedStores, @Nullable MenuCategoryType categoryType) {
        if (categoryType == null) {
            return cachedStores.stream()
                .map(StoreInfoResponse::of)
                .collect(Collectors.toList());
        }
        return cachedStores.stream()
            .filter(store -> store.hasCategory(categoryType))
            .map(StoreInfoResponse::of)
            .collect(Collectors.toList());
    }

    private static void saveAroundStoresInCached(CachedAroundStoreRepository cachedAroundStoreRepository, List<Store> nearStores, double mapLatitude, double mapLongitude, double distance) {
        List<CachedAroundStoreValue> cachedAroundStores = nearStores.stream()
            .map(store -> CachedAroundStoreValue.of(
                store.getMenuCategoriesSortedByCounts(),
                store.getId(),
                store.getLatitude(),
                store.getLongitude(),
                store.getName(),
                store.getRating(),
                store.getCreatedAt(),
                store.getUpdatedAt()
            ))
            .collect(Collectors.toList());
        cachedAroundStoreRepository.set(mapLatitude, mapLongitude, distance, cachedAroundStores);
    }

}
