package com.depromeet.threedollar.application.common.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class CursorResponseTest {

    @Test
    fun `nextCursor가 null인 경우 마지막 커서이다`() {
        // when
        val nextCursor: String? = null

        // when
        val cursor = CursorResponse.of(nextCursor)

        // then
        assertThat(cursor.nextCursor).isEqualTo(null)
        assertThat(cursor.hasMore).isFalse
    }

    @Test
    fun `nextCursor에 null이 아닌 값이 넘어오면 마지막 커서가 아니다`() {
        // when
        val nextCursor = "cursor"

        // when
        val cursor = CursorResponse.of(nextCursor)

        // then
        assertThat(cursor.nextCursor).isEqualTo(nextCursor)
        assertThat(cursor.hasMore).isTrue
    }

    @Test
    fun `마지막 커서인경우 nextCursor가 -1, hasMore이 true가 반환된다`() {
        // when
        val cursor = CursorResponse.newLastCursor()

        // then
        assertAll({
            assertThat(cursor.nextCursor).isEqualTo(-1L)
            assertThat(cursor.hasMore).isFalse
        })
    }

}
