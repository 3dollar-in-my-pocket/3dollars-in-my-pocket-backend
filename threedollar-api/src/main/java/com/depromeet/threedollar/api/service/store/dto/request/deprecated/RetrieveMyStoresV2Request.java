package com.depromeet.threedollar.api.service.store.dto.request.deprecated;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Deprecated
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyStoresV2Request {

    @Min(value = 1, message = "{common.size.min}")
    @Max(value = 100, message = "{common.size.max}")
    private int size;

    @Nullable
    private Long cursor;

    @Nullable
    private Long cachingTotalElements; // 총 가게 수를 매번 서버에서 조회하지 않고, 캐싱하기 위한 필드. (Optional)

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;

    public static RetrieveMyStoresV2Request testInstance(int size, @Nullable Long cursor, @Nullable Long cachingTotalElements, @Nullable Double latitude, @Nullable Double longitude) {
        return new RetrieveMyStoresV2Request(size, cursor, cachingTotalElements, latitude, longitude);
    }

}
