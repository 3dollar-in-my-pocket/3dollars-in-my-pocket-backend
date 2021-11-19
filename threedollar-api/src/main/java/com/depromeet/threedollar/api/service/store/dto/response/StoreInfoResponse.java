package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.common.utils.LocationDistanceUtils;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

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
    private Integer distance;
    private final List<MenuCategoryType> categories = new ArrayList<>();
    private VisitHistoryInfoResponse visitHistory;
    private Boolean isDeleted;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreInfoResponse(Long storeId, double latitude, double longitude, String storeName, double rating, Integer distance, long existsVisitsCount, long notExistsVisitsCount, boolean isDeleted) {
        this.storeId = storeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.rating = rating;
        this.distance = distance;
        this.visitHistory = VisitHistoryInfoResponse.of(existsVisitsCount, notExistsVisitsCount);
        this.isDeleted = isDeleted;
    }

    public static StoreInfoResponse testInstance(int distance, double rating) {
        return StoreInfoResponse.builder()
            .distance(distance)
            .rating(rating)
            .build();
    }

    public static StoreInfoResponse of(Store store, Double latitude, Double longitude, long existsVisitsCount, long notExistsVisitsCount) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .distance(LocationDistanceUtils.getDistance(latitude, longitude, store.getLatitude(), store.getLongitude()))
            .existsVisitsCount(existsVisitsCount)
            .notExistsVisitsCount(notExistsVisitsCount)
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

    public static StoreInfoResponse ofZeroVisitCounts(Store store) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .distance(null)
            .existsVisitsCount(0)
            .notExistsVisitsCount(0)
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

}
