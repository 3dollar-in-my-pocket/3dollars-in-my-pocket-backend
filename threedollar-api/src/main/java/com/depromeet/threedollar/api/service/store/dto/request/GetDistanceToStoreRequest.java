package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetDistanceToStoreRequest {

    @NotNull(message = "{store.mapLatitude.notNull}")
    private Double mapLatitude;

    @NotNull(message = "{store.mapLongitude.notNull}")
    private Double mapLongitude;

}
