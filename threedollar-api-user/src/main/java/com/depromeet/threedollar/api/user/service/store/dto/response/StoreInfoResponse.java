package com.depromeet.threedollar.api.user.service.store.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.redis.domain.user.store.CachedAroundStoreValue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreInfoResponse extends AuditingTimeResponse {

    private final List<MenuCategoryType> categories = new ArrayList<>();
    private Long storeId;
    private double latitude;
    private double longitude;
    private String storeName;
    private double rating;
    private Boolean isDeleted;

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

    public static StoreInfoResponse of(@NotNull CachedAroundStoreValue cachedStore) {
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

    public boolean hasMenuCategory(MenuCategoryType categoryType) {
        return categories.contains(categoryType);
    }

}
