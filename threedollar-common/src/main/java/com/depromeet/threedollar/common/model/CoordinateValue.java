package com.depromeet.threedollar.common.model;

import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
public class CoordinateValue {

    private final double latitude;

    private final double longitude;

    private CoordinateValue(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static CoordinateValue of(double latitude, double longitude) {
        return new CoordinateValue(latitude, longitude);
    }

}
