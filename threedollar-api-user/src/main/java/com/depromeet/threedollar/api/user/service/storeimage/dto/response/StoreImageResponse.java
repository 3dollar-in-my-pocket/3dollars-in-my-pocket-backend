package com.depromeet.threedollar.api.user.service.storeimage.dto.response;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.rds.user.domain.storeimage.StoreImage;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreImageResponse extends AuditingTimeResponse {

    private Long imageId;
    private String url;

    public static StoreImageResponse of(StoreImage storeImage) {
        StoreImageResponse response = new StoreImageResponse(storeImage.getId(), storeImage.getUrl());
        response.setBaseTime(storeImage);
        return response;
    }

}
