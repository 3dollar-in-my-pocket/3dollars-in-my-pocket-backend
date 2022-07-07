package com.depromeet.threedollar.domain.rds.core.model;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreLocation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StoreLocationTest {

    @Nested
    class StoreLocationInstanceTest {

        @CsvSource({
            "33.1, 124.6",
            "35.0, 128.0",
            "38.61, 131.87"
        })
        @ParameterizedTest
        void 허용된_위도와_경도_범위인경우_에러가_발생하지_않는다(double latitude, double longitude) {
            // when & then
            assertDoesNotThrow(() -> StoreLocation.of(latitude, longitude));
        }

        @CsvSource({
            "33.09, 130.0",
            "38.62, 130.0"
        })
        @ParameterizedTest
        void 허용된_위도_범위_밖인경우_INVALID_EXCEPTION(double latitude, double longitude) {
            // when & then
            assertThatThrownBy(() -> StoreLocation.of(latitude, longitude)).isInstanceOf(InvalidException.class);
        }

        @CsvSource({
            "35.0, 124.59",
            "35.0, 131.88"
        })
        @ParameterizedTest
        void 허용된_경도_범위_밖인경우_INVALID_EXCEPTION(double latitude, double longitude) {
            // when & then
            assertThatThrownBy(() -> StoreLocation.of(latitude, longitude)).isInstanceOf(InvalidException.class);
        }

    }

}
