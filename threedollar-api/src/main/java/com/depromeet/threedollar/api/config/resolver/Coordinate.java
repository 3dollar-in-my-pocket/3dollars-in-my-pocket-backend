package com.depromeet.threedollar.api.config.resolver;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Coordinate {

    private final Double latitude;

    private final Double longitude;

    public static Coordinate of(Double latitude, Double longitude) {
        return new Coordinate(latitude, longitude);
    }

}
