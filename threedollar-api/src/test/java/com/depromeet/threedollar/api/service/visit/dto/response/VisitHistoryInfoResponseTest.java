package com.depromeet.threedollar.api.service.visit.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VisitHistoryInfoResponseTest {

    @Test
    void 방문_인증한_사람이_방문하지_않은_사람보다_많은경우_인증된_가게로_표시된다() {
        // given
        int existsVisitCount = 1;
        int notExistsVisitsCount = 2;

        // when
        VisitHistoryInfoResponse response = VisitHistoryInfoResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isFalse();
    }

    @Test
    void 방문_인증한_사람이_방문하지_않은_사람보다_적은경우_인증되지_않은_가게로_표시된다() {
        // given
        int existsVisitCount = 1;
        int notExistsVisitsCount = 2;

        // when
        VisitHistoryInfoResponse response = VisitHistoryInfoResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isFalse();
    }

    @Test
    void 아무런_방문_인증을_하지_않으면_인증되지_않은_가게로_표시된다() {
        // given
        int existsVisitCount = 0;
        int notExistsVisitsCount = 0;

        // when
        VisitHistoryInfoResponse response = VisitHistoryInfoResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isFalse();
    }

}
