package com.depromeet.threedollar.api.service.store.dto.request;

import com.depromeet.threedollar.domain.domain.store.StoreRadiusDistance;
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

    public StoreRadiusDistance getDistance() {
        return StoreRadiusDistance.of(this.distance / 1000);
    }

}
