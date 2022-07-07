package com.depromeet.threedollar.common.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeUtilsTest {

    @Test
    void 타임스탬프를_LocalDateTime으로_변환한다() {
        // given
        long timestamp = 1655906587L;

        // when
        LocalDateTime localDateTime = LocalDateTimeUtils.epochToLocalDateTime(timestamp);

        // then
        assertThat(localDateTime).isEqualTo(LocalDateTime.of(2022, 6, 22, 23, 3, 7));
    }

    @Test
    void LocalDateTime을_timestamp로_변환한다() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2022, 6, 22, 23, 3, 7);

        // when
        long timestamp = LocalDateTimeUtils.toTimestamp(localDateTime);

        assertThat(timestamp).isEqualTo(1655906587L);
    }

}
