package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreWithVisitsResponse extends AuditingTimeResponse {

    private Long storeId;
    private double latitude;
    private double longitude;
    private String storeName;
    private double rating;
    private Boolean isDeleted;
    private final List<MenuCategoryType> categories = new ArrayList<>();

    private VisitHistoryCountsResponse visitHistory;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreWithVisitsResponse(Long storeId, double latitude, double longitude, String storeName, double rating, Boolean isDeleted, long existsVisitsCount, long notExistsVisitsCount) {
        this.storeId = storeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.rating = rating;
        this.isDeleted = isDeleted;
        this.visitHistory = VisitHistoryCountsResponse.of(existsVisitsCount, notExistsVisitsCount);
    }

    public static StoreWithVisitsResponse of(Store store, long existsVisitsCount, long notExistsVisitsCount) {
        StoreWithVisitsResponse response = StoreWithVisitsResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .rating(store.getRating())
            .existsVisitsCount(existsVisitsCount)
            .notExistsVisitsCount(notExistsVisitsCount)
            .isDeleted(store.isDeleted())
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setBaseTime(store);
        return response;
    }

}
