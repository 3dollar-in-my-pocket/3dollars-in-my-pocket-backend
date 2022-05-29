package com.depromeet.threedollar.common.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import com.depromeet.threedollar.common.exception.model.InvalidException

internal class BusinessNumberTest {

    @ValueSource(strings = ["125-86-31602", "527-88-00686", "120-87-65763"])
    @ParameterizedTest
    fun `정상적인 사업자 번호면 유효성 검사를 통과한다`(number: String) {
        // when & then
        assertDoesNotThrow { BusinessNumber.of(number) }
    }

    @ValueSource(strings = ["01-12-12345", "011-1-12345", "011-12-1234", "011-122-123456", "ABC-01-12345"])
    @ParameterizedTest
    fun `사업자 번호 형식에 맞지 않으면 InvalidException이 발생한다`(number: String) {
        // when & then
        assertThatThrownBy { BusinessNumber.of(number) }.isInstanceOf(InvalidException::class.java)
    }

    @Test
    fun `사업자 번호 형식은 맞지만 체크섬이 깨지는 경우 InvalidException이 발생한다`() {
        // given
        val number = "111-11-11111"

        // when & then
        assertThatThrownBy { BusinessNumber.of(number) }.isInstanceOf(InvalidException::class.java)
    }

}
