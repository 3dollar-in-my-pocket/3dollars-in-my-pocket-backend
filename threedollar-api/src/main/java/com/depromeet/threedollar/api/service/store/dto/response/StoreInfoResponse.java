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

    @Builder(access = AccessLevel.PRIVATE)
    private StoreInfoResponse(Long storeId, double latitude, double longitude, String storeName, double rating, Integer distance, long existsVisitsCount, long notExistsVisitsCount) {
        this.storeId = storeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.rating = rating;
        this.distance = distance;
        this.visitHistory = VisitHistoryInfoResponse.of(existsVisitsCount, notExistsVisitsCount);
    }

    public static StoreInfoResponse testInstance(int distance, double rating) {
        return StoreInfoResponse.builder()
            .distance(distance)
            .rating(rating)
            .build();
    }

    public static StoreInfoResponse of(Store store, double latitude, double longitude, long existsVisitsCount, long notExistsVisitsCount) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .distance(LocationDistanceUtils.getDistance(latitude, longitude, store.getLatitude(), store.getLongitude()))
            .existsVisitsCount(existsVisitsCount)
            .notExistsVisitsCount(notExistsVisitsCount)
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

    public static StoreInfoResponse of(Store store, long existsVisitsCount, long notExistsVisitsCount) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .distance(null)
            .existsVisitsCount(existsVisitsCount)
            .notExistsVisitsCount(notExistsVisitsCount)
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

    public static StoreInfoResponse ofWithOutVisitsCount(Store store) {
        StoreInfoResponse response = StoreInfoResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .distance(null)
            .existsVisitsCount(0)
            .notExistsVisitsCount(0)
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

}
