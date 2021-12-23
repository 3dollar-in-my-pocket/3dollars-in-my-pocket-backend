package com.depromeet.threedollar.api.config.resolver;

import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Coordinate {

    private final double latitude;

    private final double longitude;

    public static Coordinate of(double latitude, double longitude) {
        return new Coordinate(latitude, longitude);
    }

}
