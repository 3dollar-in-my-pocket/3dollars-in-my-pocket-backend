package com.depromeet.threedollar.api.vendor.service.store.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.vendor.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.common.model.LocationValue;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.common.utils.distance.LocationDistanceUtils;
import com.depromeet.threedollar.domain.rds.vendor.collection.visit.VisitHistoryCounter;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreWithVisitsAndDistanceResponse extends AuditingTimeResponse {

    private Long storeId;

    private double latitude;

    private double longitude;

    private String storeName;

    private double rating;

    @Nullable
    private Boolean isDeleted;

    private final List<UserMenuCategoryType> categories = new ArrayList<>();

    private int distance;

    private VisitHistoryCountsResponse visitHistory;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreWithVisitsAndDistanceResponse(StoreInfoResponse store, int distance, long existsVisitsCount, long notExistsVisitsCount, boolean isDeleted) {
        this.storeId = store.getStoreId();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.storeName = store.getStoreName();
        this.rating = store.getRating();
        this.distance = distance;
        this.visitHistory = VisitHistoryCountsResponse.of(existsVisitsCount, notExistsVisitsCount);
        this.isDeleted = isDeleted;
    }

    public static StoreWithVisitsAndDistanceResponse of(@NotNull StoreInfoResponse store, LocationValue deviceLocation, VisitHistoryCounter visitsCounter) {
        StoreWithVisitsAndDistanceResponse response = StoreWithVisitsAndDistanceResponse.builder()
            .store(store)
            .distance(LocationDistanceUtils.getDistance(deviceLocation, LocationValue.of(store.getLatitude(), store.getLongitude())))
            .existsVisitsCount(visitsCounter.getStoreExistsVisitsCount(store.getStoreId()))
            .notExistsVisitsCount(visitsCounter.getStoreNotExistsVisitsCount(store.getStoreId()))
            .isDeleted(store.getIsDeleted())
            .build();
        response.categories.addAll(store.getCategories());
        response.setAuditingTime(store.getCreatedAt(), store.getUpdatedAt());
        return response;
    }

}
