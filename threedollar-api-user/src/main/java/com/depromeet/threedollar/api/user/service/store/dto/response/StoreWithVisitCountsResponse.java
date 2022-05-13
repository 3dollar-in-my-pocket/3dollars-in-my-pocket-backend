package com.depromeet.threedollar.api.user.service.store.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreWithVisitCountsResponse extends AuditingTimeResponse {

    private final List<MenuCategoryType> categories = new ArrayList<>();
    private Long storeId;
    private double latitude;
    private double longitude;
    private String storeName;
    private double rating;
    private Boolean isDeleted;
    private VisitHistoryCountsResponse visitHistory;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreWithVisitCountsResponse(Store store, long existsVisitsCount, long notExistsVisitsCount) {
        this.storeId = store.getId();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.storeName = store.getName();
        this.rating = store.getRating();
        this.isDeleted = store.isDeleted();
        this.visitHistory = VisitHistoryCountsResponse.of(existsVisitsCount, notExistsVisitsCount);
    }

    public static StoreWithVisitCountsResponse of(@NotNull Store store, long existsVisitsCount, long notExistsVisitsCount) {
        StoreWithVisitCountsResponse response = StoreWithVisitCountsResponse.builder()
            .store(store)
            .existsVisitsCount(existsVisitsCount)
            .notExistsVisitsCount(notExistsVisitsCount)
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.setAuditingTimeByEntity(store);
        return response;
    }

}
