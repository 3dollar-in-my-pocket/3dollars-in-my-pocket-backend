package com.depromeet.threedollar.common.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExternalIdTest {

    @Test
    void 원본의_ID로_인코딩된_ID를_생성한다() {
        // given
        long id = 1000L;

        // when
        ExternalId externalId = ExternalId.toInternal(id);

        // then
        assertThat(externalId.getExternalId()).isNotNull();
        assertThat(externalId.getInternalId()).isEqualTo(id);
    }

}
