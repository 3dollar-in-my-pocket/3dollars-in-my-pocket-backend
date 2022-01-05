package com.depromeet.threedollar.common.model;

import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CoordinateValue {

    private final double latitude;

    private final double longitude;

    public static CoordinateValue of(double latitude, double longitude) {
        return new CoordinateValue(latitude, longitude);
    }

}
