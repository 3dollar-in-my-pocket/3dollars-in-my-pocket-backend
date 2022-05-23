package com.depromeet.threedollar.domain.rds.vendor.domain.store.projection;

import java.time.LocalDateTime;

import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreImageStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class StoreImageProjection {

    private final Long id;
    private final Long storeId;
    private final Long userId;
    private final String url;
    private final StoreImageStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @QueryProjection
    public StoreImageProjection(Long id, Long storeId, Long userId, String url, StoreImageStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
        this.url = url;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
