package com.depromeet.threedollar.api.user.service.store;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOTFOUND_STORE;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.user.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.projection.StoreWithMenuProjection;
import com.depromeet.threedollar.domain.redis.domain.user.store.AroundUserStoresCacheRepository;
import com.depromeet.threedollar.domain.redis.domain.user.store.UserStoreCacheModel;

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

    static List<StoreInfoResponse> findAroundStoresFilerByCategory(StoreRepository storeRepository, AroundUserStoresCacheRepository aroundUserStoresCacheRepository,
                                                                   double mapLatitude, double mapLongitude, double distance, @Nullable UserMenuCategoryType categoryType) {
        List<StoreInfoResponse> aroundStores = findAroundStores(storeRepository, aroundUserStoresCacheRepository, mapLatitude, mapLongitude, distance);
        if (categoryType == null) {
            return aroundStores;
        }
        return aroundStores.stream()
            .filter(store -> store.hasMenuCategory(categoryType))
            .collect(Collectors.toList());
    }

    private static List<StoreInfoResponse> findAroundStores(StoreRepository storeRepository, AroundUserStoresCacheRepository aroundUserStoresCacheRepository, double mapLatitude, double mapLongitude, double distance) {
        List<UserStoreCacheModel> aroundStoresInCache = aroundUserStoresCacheRepository.get(mapLatitude, mapLongitude, distance);
        if (aroundStoresInCache != null) {
            return aroundStoresInCache.stream()
                .map(StoreInfoResponse::of)
                .collect(Collectors.toList());
        }
        List<StoreWithMenuProjection> aroundStoresInDB = storeRepository.findStoresByLocationLessThanDistance(mapLatitude, mapLongitude, distance);
        saveAroundStoresInCached(aroundUserStoresCacheRepository, aroundStoresInDB, mapLatitude, mapLongitude, distance);
        return aroundStoresInDB.stream()
            .map(StoreInfoResponse::of)
            .collect(Collectors.toList());
    }

    private static void saveAroundStoresInCached(AroundUserStoresCacheRepository aroundUserStoresCacheRepository, List<StoreWithMenuProjection> nearStores, double mapLatitude, double mapLongitude, double distance) {
        List<UserStoreCacheModel> cachedAroundStores = nearStores.stream()
            .map(store -> UserStoreCacheModel.of(
                store.getMenuCategoriesSortedByCounts(),
                store.getId(),
                store.getLatitude(),
                store.getLongitude(),
                store.getName(),
                store.getRating(),
                store.getCreatedAt(),
                store.getUpdatedAt()
            )).collect(Collectors.toList());
        aroundUserStoresCacheRepository.set(mapLatitude, mapLongitude, distance, cachedAroundStores);
    }

}
