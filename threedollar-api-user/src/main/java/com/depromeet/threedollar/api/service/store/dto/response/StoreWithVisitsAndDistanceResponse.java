package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.common.utils.LocationDistanceUtils;
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import com.depromeet.threedollar.domain.user.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.user.domain.store.StorePromotion;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    private StorePromotionResponse promotion;

    private final List<MenuCategoryType> categories = new ArrayList<>();

    private int distance;

    private VisitHistoryCountsResponse visitHistory;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreWithVisitsAndDistanceResponse(Long storeId, double latitude, double longitude, String storeName, double rating, int distance,
                                               StorePromotion promotion, long existsVisitsCount, long notExistsVisitsCount, boolean isDeleted) {
        this.storeId = storeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.rating = rating;
        this.distance = distance;
        this.promotion = StorePromotionResponse.of(promotion);
        this.visitHistory = VisitHistoryCountsResponse.of(existsVisitsCount, notExistsVisitsCount);
        this.isDeleted = isDeleted;
    }

    public static StoreWithVisitsAndDistanceResponse of(@NotNull Store store, double latitude, double longitude, VisitHistoryCounter visitsCounter) {
        StoreWithVisitsAndDistanceResponse response = StoreWithVisitsAndDistanceResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .promotion(store.getPromotion())
            .distance(LocationDistanceUtils.getDistance(latitude, longitude, store.getLatitude(), store.getLongitude()))
            .existsVisitsCount(visitsCounter.getStoreExistsVisitsCount(store.getId()))
            .notExistsVisitsCount(visitsCounter.getStoreNotExistsVisitsCount(store.getId()))
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

}
