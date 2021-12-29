package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckExistsStoresNearbyRequest {

    @NotNull(message = "{store.distance.notNull}")
    private Double distance;

    public static CheckExistsStoresNearbyRequest testInstance(double distance) {
        return new CheckExistsStoresNearbyRequest(distance);
    }

}
