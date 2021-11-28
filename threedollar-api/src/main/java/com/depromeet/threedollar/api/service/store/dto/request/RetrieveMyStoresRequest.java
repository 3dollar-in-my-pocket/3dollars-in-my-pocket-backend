package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyStoresRequest {

    @Min(value = 1, message = "{common.size.min}")
    @Max(value = 100, message = "{common.size.max}")
    private int size;

    @Nullable
    private Long cursor;

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;

    public static RetrieveMyStoresRequest testInstance(int size, @Nullable Long cursor,  @Nullable Double latitude, @Nullable Double longitude) {
        return new RetrieveMyStoresRequest(size, cursor, latitude, longitude);
    }

}
