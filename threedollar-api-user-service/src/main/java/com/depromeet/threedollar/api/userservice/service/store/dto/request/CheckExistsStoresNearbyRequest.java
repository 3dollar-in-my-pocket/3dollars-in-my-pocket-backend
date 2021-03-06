package com.depromeet.threedollar.api.userservice.service.store.dto.request;

import com.depromeet.threedollar.common.utils.distance.LookupRadiusDistanceLimiter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckExistsStoresNearbyRequest {

    @PositiveOrZero(message = "{store.distance.positiveOrZero}")
    @NotNull(message = "{store.distance.notNull}")
    private Double distance;

    @Builder(builderMethodName = "testBuilder")
    private CheckExistsStoresNearbyRequest(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return LookupRadiusDistanceLimiter.fromMtoKm(distance);
    }

}
