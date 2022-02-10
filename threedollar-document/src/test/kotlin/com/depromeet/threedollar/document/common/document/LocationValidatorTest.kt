package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.InvalidException
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class LocationValidatorTest {

    @CsvSource("33.09, 130.0", "38.62, 130.0")
    @ParameterizedTest
    fun 허용된_위도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(latitude: Double, longitude: Double) {
        // when & then
        Assertions.assertThatThrownBy { LocationValidator.validate(latitude, longitude) }.isInstanceOf(InvalidException::class.java)
    }

    @CsvSource("35.0, 124.59", "35.0, 131.88")
    @ParameterizedTest
    fun 허용된_경도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(latitude: Double, longitude: Double) {
        // when & then
        Assertions.assertThatThrownBy { LocationValidator.validate(latitude, longitude) }.isInstanceOf(InvalidException::class.java)
    }

}
