package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreImageProjection;
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
