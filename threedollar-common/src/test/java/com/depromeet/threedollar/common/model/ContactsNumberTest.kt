package com.depromeet.threedollar.common.model

import com.depromeet.threedollar.common.exception.model.InvalidException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ContactsNumberTest {

    @ValueSource(strings = [
        "02-123-1234",
        "031-1234-1234",
        "031-123-1234",
        "010-1234-1234"
    ])
    @ParameterizedTest
    fun `연락처 번호 형식에 일치하는 경우 유효성 검사를 통과한다`(number: String) {
        // when & then
        assertDoesNotThrow { ContactsNumber.of(number) }
    }

    @ValueSource(strings = [
        "01012341234",
        "",
        "1234"
    ])
    @ParameterizedTest
    fun `연락처 번호 형식에 어긋나는 경우 InvalidException이 발생한다`(number: String) {
        // when & then
        assertThatThrownBy { ContactsNumber.of(number) }.isInstanceOf(InvalidException::class.java)
    }

}
