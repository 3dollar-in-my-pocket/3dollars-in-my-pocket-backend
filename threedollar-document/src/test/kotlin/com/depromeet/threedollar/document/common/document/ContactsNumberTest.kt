package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.InvalidException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ContactsNumberTest {

    @ValueSource(strings = ["02-123-1234", "031-1234-1234", "031-123-1234", "010-1234-1234"])
    @ParameterizedTest
    fun `정상적인 연락처 번호인 경우 InvalidException이 발생하지 않는다`(number: String) {
        // when & then
        assertDoesNotThrow { ContactsNumber.of(number) }
    }

    @ValueSource(strings = ["01012341234"])
    @ParameterizedTest
    fun `연락처 번호 유효성 검증 실패하는 케이스`(number: String) {
        // when & then
        assertThatThrownBy { ContactsNumber.of(number) }.isInstanceOf(InvalidException::class.java)
    }

}
