package com.depromeet.threedollar.common.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DecodedIdTest {

    @Test
    void 원본의_ID로_인코딩된_ID를_생성한다() {
        // given
        long id = 1000L;

        // when
        DecodedId decodedId = DecodedId.toInternal(id);

        // then
        assertThat(decodedId.getExternalId()).isNotNull();
        assertThat(decodedId.getInternalId()).isEqualTo(id);
    }

}
