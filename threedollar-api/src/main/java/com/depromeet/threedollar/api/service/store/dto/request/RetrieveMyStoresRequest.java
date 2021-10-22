package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyStoresRequest {

    @Min(value = 1, message = "{common.size.min}")
    private int size;

    private Long cursor;

    private Long cachingTotalElements; // 총 가게 수를 매번 서버에서 조회하지 않고, 캐싱하기 위한 필드. (Optional)

    private Double latitude;

    private Double longitude;

    public static RetrieveMyStoresRequest testInstance(int size, Long cursor, Long cachingTotalElements, Double latitude, Double longitude) {
        return new RetrieveMyStoresRequest(size, cursor, cachingTotalElements, latitude, longitude);
    }

}
