package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyStoresRequest {

    @Min(value = 1, message = "{common.size.min}")
    private int size;

    @Min(value = 0, message = "{common.page.min}")
    private int page;

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    public static RetrieveMyStoresRequest testInstance(int size, int page, double latitude, double longitude) {
        return new RetrieveMyStoresRequest(size, page, latitude, longitude);
    }

}
