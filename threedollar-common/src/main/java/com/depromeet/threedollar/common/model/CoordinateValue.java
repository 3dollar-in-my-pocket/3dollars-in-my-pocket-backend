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
public class CoordinateValue {

    private double latitude;

    private double longitude;

    @Builder(access = AccessLevel.PRIVATE)
    private CoordinateValue(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static CoordinateValue of(double latitude, double longitude) {
        return CoordinateValue.builder()
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

}
