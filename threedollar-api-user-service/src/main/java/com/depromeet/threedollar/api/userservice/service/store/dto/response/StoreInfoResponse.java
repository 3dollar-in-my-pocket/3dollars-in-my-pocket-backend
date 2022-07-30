package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import com.depromeet.threedollar.domain.redis.domain.userservice.store.UserStoreCacheModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreInfoResponse extends AuditingTimeResponse {

    private Long storeId;

    private double latitude;

    private double longitude;

    private String storeName;

    private double rating;

    private Boolean isDeleted;

    private final List<UserMenuCategoryType> categories = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private StoreInfoResponse(Long storeId, double latitude, double longitude, String storeName, double rating, Boolean isDeleted) {
        this.storeId = storeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.rating = rating;
        this.isDeleted = isDeleted;
    }

    public static StoreInfoResponse of(@NotNull Store store) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setAuditingTimeByEntity(store);
        return response;
    }

    public static StoreInfoResponse of(@NotNull StoreWithMenuProjection store) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setAuditingTime(store.getCreatedAt(), store.getUpdatedAt());
        return response;
    }

    public static StoreInfoResponse of(@NotNull UserStoreCacheModel cachedStore) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(cachedStore.getStoreId())
            .latitude(cachedStore.getLatitude())
            .longitude(cachedStore.getLongitude())
            .storeName(cachedStore.getStoreName())
            .rating(cachedStore.getRating())
            .isDeleted(false)
            .build();
        response.categories.addAll(cachedStore.getCategories());
        response.setAuditingTime(cachedStore.getCreatedAt(), cachedStore.getUpdatedAt());
        return response;
    }

    public boolean hasMenuCategory(UserMenuCategoryType categoryType) {
        return categories.contains(categoryType);
    }

}
