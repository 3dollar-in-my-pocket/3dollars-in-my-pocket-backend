package com.depromeet.threedollar.domain.mongo.common.domain

import com.depromeet.threedollar.common.exception.model.InvalidException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class BusinessNumberTest {

    @Test
    fun `정상적인 사업자 번호면 InvalidException이 발생하지 않는다`() {
        // given
        val number = "011-12-12345"

        // when & then
        assertDoesNotThrow { BusinessNumber.of(number) }
    }

    @ValueSource(strings = ["01-12-12345", "011-1-12345", "011-12-1234", "011-122-123456", "ABC-01-12345"])
    @ParameterizedTest
    fun `잘못된 사업자 번호면 InvalidException이 발생한다`(number: String) {
        // when & then
        assertThatThrownBy { BusinessNumber.of(number) }.isInstanceOf(InvalidException::class.java)
    }

}
