package com.depromeet.threedollar.domain.domain.common;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocationTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 인스턴스_생성 {

        @CsvSource({
            "33, 124",
            "43, 132"
        })
        @ParameterizedTest
        void 위도와_경도로_이루어진_위치_값_객체를_생성한다(double latitude, double longitude) {
            // when
            Location location = Location.of(latitude, longitude);

            // then
            assertThat(location.getLatitude()).isEqualTo(latitude);
            assertThat(location.getLongitude()).isEqualTo(longitude);
        }

        @CsvSource({
            "32.999, 124",
            "43.1, 124"
        })
        @ParameterizedTest
        void 허용된_위도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(double latitude, double longitude) {
            // when & then
            assertThatThrownBy(() -> Location.of(latitude, longitude)).isInstanceOf(ValidationException.class);
        }

        @CsvSource({
            "33, 132.1",
            "33, 123.9"
        })
        @ParameterizedTest
        void 허용된_경도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(double latitude, double longitude) {
            // when & then
            assertThatThrownBy(() -> Location.of(latitude, longitude)).isInstanceOf(ValidationException.class);
        }

    }

}
