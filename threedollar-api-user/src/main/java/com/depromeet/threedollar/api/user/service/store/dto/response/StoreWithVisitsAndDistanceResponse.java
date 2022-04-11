package com.depromeet.threedollar.api.user.service.store.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.common.model.CoordinateValue;
import com.depromeet.threedollar.common.utils.LocationDistanceUtils;
import com.depromeet.threedollar.domain.rds.user.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreWithVisitsAndDistanceResponse extends AuditingTimeResponse {

    private final List<MenuCategoryType> categories = new ArrayList<>();
    private Long storeId;
    private double latitude;
    private double longitude;
    private String storeName;
    private double rating;
    @Nullable
    private Boolean isDeleted;
    private int distance;

    private VisitHistoryCountsResponse visitHistory;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreWithVisitsAndDistanceResponse(Store store, int distance, long existsVisitsCount, long notExistsVisitsCount, boolean isDeleted) {
        this.storeId = store.getId();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.storeName = store.getName();
        this.rating = store.getRating();
        this.distance = distance;
        this.visitHistory = VisitHistoryCountsResponse.of(existsVisitsCount, notExistsVisitsCount);
        this.isDeleted = isDeleted;
    }

    public static StoreWithVisitsAndDistanceResponse of(@NotNull Store store, CoordinateValue geoCoordinate, VisitHistoryCounter visitsCounter) {
        StoreWithVisitsAndDistanceResponse response = StoreWithVisitsAndDistanceResponse.builder()
            .store(store)
            .distance(LocationDistanceUtils.getDistance(geoCoordinate, CoordinateValue.of(store.getLatitude(), store.getLongitude())))
            .existsVisitsCount(visitsCounter.getStoreExistsVisitsCount(store.getId()))
            .notExistsVisitsCount(visitsCounter.getStoreNotExistsVisitsCount(store.getId()))
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setAuditingTimeByEntity(store);
        return response;
    }

}
