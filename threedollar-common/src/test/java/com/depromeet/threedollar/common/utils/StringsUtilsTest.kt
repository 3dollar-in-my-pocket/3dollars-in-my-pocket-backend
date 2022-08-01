package com.depromeet.threedollar.common.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource

internal class StringsUtilsTest {

    @NullSource
    @ParameterizedTest
    fun `defaultIfNull - NULL인경우 디폴트 값이 반환된다`(value: String?) {
        // given
        val defaultValue = "기본 값"

        // when
        val sut = StringsUtils.defaultIfNull(value = value, defaultValue = defaultValue)

        // then
        assertThat(sut).isEqualTo(defaultValue)
    }

    @ValueSource(strings = [
        "",
        " ",
        "NOT NULL"
    ])
    @ParameterizedTest
    fun `defaultIfNull - NULL이 아닌 경우 입력 값이 반환된다`(value: String) {
        // given
        val defaultValue = "기본 값"

        // when
        val sut = StringsUtils.defaultIfNull(value = value, defaultValue = defaultValue)

        // then
        assertThat(sut).isEqualTo(value)
    }

    @NullAndEmptySource
    @ParameterizedTest
    fun `defaultIfBlank - NULL이거나 Blank인 경우 디폴트 값이 반환된다`(value: String?) {
        // given
        val defaultValue = "기본 값"

        // when
        val sut = StringsUtils.defaultIfBlank(value = value, defaultValue = defaultValue)

        // then
        assertThat(sut).isEqualTo(defaultValue)
    }

    @Test
    fun `defaultIfBlank - NULL이 아닌 경우 입력 값이 반환된다`() {
        // given
        val value = "NOT NULL"

        // when
        val sut = StringsUtils.defaultIfBlank(value = value, defaultValue = "기본값")

        // then
        assertThat(sut).isEqualTo(value)
    }

}
