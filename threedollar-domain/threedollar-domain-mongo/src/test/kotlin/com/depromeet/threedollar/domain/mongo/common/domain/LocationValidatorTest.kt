package com.depromeet.threedollar.domain.mongo.common.domain

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import com.depromeet.threedollar.common.exception.model.InvalidException

internal class LocationValidatorTest {

    @CsvSource("33.1, 124.6", "38.61, 131.87")
    @ParameterizedTest
    fun `정상 위도 경도내인경우 InvalidException이 발생하지 않는다`(latitude: Double, longitude: Double) {
        // when & then
        assertDoesNotThrow { LocationValidator.validate(latitude, longitude) }
    }

    @CsvSource("33.09, 130.0", "38.62, 130.0")
    @ParameterizedTest
    fun 허용된_위도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(latitude: Double, longitude: Double) {
        // when & then
        assertThatThrownBy { LocationValidator.validate(latitude, longitude) }.isInstanceOf(InvalidException::class.java)
    }

    @CsvSource("35.0, 124.59", "35.0, 131.88")
    @ParameterizedTest
    fun 허용된_경도_범위_밖인경우_VALIDATION_LATITUDE_EXEPTION(latitude: Double, longitude: Double) {
        // when & then
        assertThatThrownBy { LocationValidator.validate(latitude, longitude) }.isInstanceOf(InvalidException::class.java)
    }

}
