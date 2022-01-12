package com.depromeet.threedollar.domain.user.domain.store.projection;

import com.depromeet.threedollar.domain.user.domain.store.StoreType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
public class StoreWithReportedCountProjection {

    private final Long storeId;
    private final String storeName;
    private final double latitude;
    private final double longitude;
    private final StoreType type;
    private final double rating;
    private final LocalDateTime storeCreatedAt;
    private final LocalDateTime storeUpdatedAt;
    private final long reportsCount;

    @QueryProjection
    public StoreWithReportedCountProjection(Long storeId, String storeName, double latitude, double longitude, StoreType type,
                                            double rating, LocalDateTime storeCreatedAt, LocalDateTime storeUpdatedAt, long reportsCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.rating = rating;
        this.storeCreatedAt = storeCreatedAt;
        this.storeUpdatedAt = storeUpdatedAt;
        this.reportsCount = reportsCount;
    }

}
