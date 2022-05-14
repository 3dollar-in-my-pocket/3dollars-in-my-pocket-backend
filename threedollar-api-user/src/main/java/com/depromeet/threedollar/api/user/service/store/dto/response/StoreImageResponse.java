package com.depromeet.threedollar.api.user.service.store.dto.response;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;

import com.depromeet.threedollar.domain.rds.user.domain.store.projection.StoreImageProjection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreImageResponse extends AuditingTimeResponse {

    private Long imageId;
    private String url;

    public static StoreImageResponse of(StoreImage storeImage) {
        StoreImageResponse response = new StoreImageResponse(storeImage.getId(), storeImage.getUrl());
        response.setAuditingTimeByEntity(storeImage);
        return response;
    }

    public static StoreImageResponse of(StoreImageProjection storeImage) {
        StoreImageResponse response = new StoreImageResponse(storeImage.getId(), storeImage.getUrl());
        response.setAuditingTime(storeImage.getCreatedAt(), storeImage.getUpdatedAt());
        return response;
    }

}
