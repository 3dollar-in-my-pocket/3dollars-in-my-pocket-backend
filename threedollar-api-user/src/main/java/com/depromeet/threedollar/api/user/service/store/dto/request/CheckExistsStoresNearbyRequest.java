package com.depromeet.threedollar.api.user.service.store.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRadiusDistance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckExistsStoresNearbyRequest {

    @PositiveOrZero(message = "{store.distance.positiveOrZero}")
    @NotNull(message = "{store.distance.notNull}")
    private Double distance;

    public static CheckExistsStoresNearbyRequest testInstance(double distance) {
        return new CheckExistsStoresNearbyRequest(distance);
    }

    public StoreRadiusDistance getDistance() {
        return StoreRadiusDistance.of(this.distance / 1000);
    }

}
