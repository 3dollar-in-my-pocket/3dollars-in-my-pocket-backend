package com.depromeet.threedollar.api.core.common.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PagingResponseTest {

    @Test
    fun `아무 데이터도 없는 경우 총 0페이지가 된다`() {
        // given
        val totalSize = 0L
        val perSize = 3

        // when
        val response = PagingResponse.of(perSize = perSize, totalSize = totalSize)

        // then
        assertThat(response.totalPage).isZero;
    }

    @ValueSource(longs = [1, 2, 3])
    @ParameterizedTest
    fun `1보다 크고 요구한 갯수보다 같거나 적은 데이터가 있는 경우 총 1페이지가 된다`(totalSize: Long) {
        // given
        val perSize = 3

        // when
        val response = PagingResponse.of(perSize = perSize, totalSize = totalSize)

        // then
        assertThat(response.totalPage).isEqualTo(1)
    }

    @ValueSource(longs = [4, 5, 6])
    @ParameterizedTest
    fun `총 2페이지가된다`(totalSize: Long) {
        // given
        val perSize = 3

        // when
        val response = PagingResponse.of(perSize = perSize, totalSize = totalSize)

        // then
        assertThat(response.totalPage).isEqualTo(2)
    }

}
