package com.depromeet.threedollar.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Base32UtilsTest {

    @Test
    void 인코딩한_값을_디코딩해서_원복한다() {
        // given
        long value = 1L;

        String encodingValue = Base32Utils.encode(value);

        // when
        long decodingValue = Base32Utils.decode(encodingValue);

        // then
        assertThat(decodingValue).isEqualTo(value);
    }

    @Test
    void 값이_다른경우_다른_값이_나온다() {
        // when
        String one = Base32Utils.encode(1L);
        String two = Base32Utils.encode(2L);

        // then
        assertThat(two).isNotEqualTo(one);
    }

}
