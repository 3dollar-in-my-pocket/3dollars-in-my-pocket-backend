package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.InvalidException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class BusinessNumberTest {

    @Test
    fun `사업자 번호 유효성 검사`() {
        // given
        val number = "011-12-12345"

        // when
        val businessNumber = BusinessNumber.of(number)

        // then
        assertThat(businessNumber.getNumberWithSeparator()).isEqualTo(number)
    }

    @ValueSource(strings = ["01-12-12345", "011-1-12345", "011-12-1234", "011-122-123456", "ABC-01-12345"])
    @ParameterizedTest
    fun `사업자 번호 유효성 검사 1`(number: String) {
        // when & then
        assertThatThrownBy { BusinessNumber.of(number) }.isInstanceOf(InvalidException::class.java)
    }

}
