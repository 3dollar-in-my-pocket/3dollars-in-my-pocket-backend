package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.ValidationException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class LocationValidatorTest {

    @CsvSource("33.09, 130.0", "38.62, 130.0")
    @ParameterizedTest
    fun 허용된_위도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(latitude: Double, longitude: Double) {
        // when & then
        Assertions.assertThatThrownBy { LocationValidator.validate(latitude, longitude) }.isInstanceOf(ValidationException::class.java)
    }

    @CsvSource("35.0, 124.59", "35.0, 131.88")
    @ParameterizedTest
    fun 허용된_경도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(latitude: Double, longitude: Double) {
        // when & then
        Assertions.assertThatThrownBy { LocationValidator.validate(latitude, longitude) }.isInstanceOf(ValidationException::class.java)
    }

    @Test
    fun 위도_경도_둘다_null인경우_한번도_영업_시작을_하지_않은_가게로_OK() {
        LocationValidator.validate(null, null)
    }

    @Test
    fun 위도만_null인경우_VALIDATION_EXCEPTION() {
        // when & then
        Assertions.assertThatThrownBy { LocationValidator.validate(38.0, null) }.isInstanceOf(ValidationException::class.java)
    }

    @Test
    fun 경도만_null인경우_VALIDATION_EXCEPTION() {
        // when & then
        Assertions.assertThatThrownBy { LocationValidator.validate(null, 128.0) }.isInstanceOf(ValidationException::class.java)
    }

}
