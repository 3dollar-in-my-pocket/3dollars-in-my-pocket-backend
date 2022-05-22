package com.depromeet.threedollar.common.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationValue {

    private double latitude;

    private double longitude;

    @Builder(access = AccessLevel.PRIVATE)
    private LocationValue(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationValue of(double latitude, double longitude) {
        return LocationValue.builder()
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

}
