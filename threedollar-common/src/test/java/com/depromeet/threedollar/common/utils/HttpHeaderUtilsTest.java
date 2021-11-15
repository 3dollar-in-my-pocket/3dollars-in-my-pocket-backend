package com.depromeet.threedollar.common.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpHeaderUtilsTest {

    @Test
    void 토큰을_BEARER_타입으로_변환한다() {
        // given
        String token = "token";

        // when
        String result = HttpHeaderUtils.withBearerToken(token);

        // then
        assertThat(result).isEqualTo("Bearer token");
    }

}
