package com.depromeet.threedollar.domain.common.domain;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 인스턴스_생성 {

        @CsvSource({
            "33.1, 124.6",
            "35.0, 128.0",
            "38.61, 131.87"
        })
        @ParameterizedTest
        void 허용된_위도와_경도_내인경우_VALIDATION_EXCEPTION이_발생하지_않는다(double latitude, double longitude) {
            // when & then
            assertDoesNotThrow(() -> Location.of(latitude, longitude));
        }

        @CsvSource({
            "33.09, 130.0",
            "38.62, 130.0"
        })
        @ParameterizedTest
        void 허용된_위도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(double latitude, double longitude) {
            // when & then
            assertThatThrownBy(() -> Location.of(latitude, longitude)).isInstanceOf(InvalidException.class);
        }

        @CsvSource({
            "35.0, 124.59",
            "35.0, 131.88"
        })
        @ParameterizedTest
        void 허용된_경도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(double latitude, double longitude) {
            // when & then
            assertThatThrownBy(() -> Location.of(latitude, longitude)).isInstanceOf(InvalidException.class);
        }

    }

}
