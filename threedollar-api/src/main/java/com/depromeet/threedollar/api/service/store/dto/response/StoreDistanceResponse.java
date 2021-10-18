package com.depromeet.threedollar.api.service.store.dto.response;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDistanceResponse {

    private int distance;

    public static StoreDistanceResponse of(int distance) {
        return new StoreDistanceResponse(distance);
    }

}
