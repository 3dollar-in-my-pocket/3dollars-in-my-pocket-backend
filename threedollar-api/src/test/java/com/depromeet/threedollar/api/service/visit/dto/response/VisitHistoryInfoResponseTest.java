package com.depromeet.threedollar.api.service.visit.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VisitHistoryInfoResponseTest {

    @Test
    void 한명이라도_가게_방문_인증이_했을경우_인증된_가게로_표시된다() {
        // given
        int existsVisitCount = 2;
        int notExistsVisitsCount = 1;

        // when
        VisitHistoryInfoResponse response = VisitHistoryInfoResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isTrue();
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

    @Test
    void 아무도_가게_방문_인증을_하지_않은경우_인증되지_않은_가게라고_표시된다() {
        // given
        int existsVisitCount = 0;
        int notExistsVisitsCount = 1;

        // when
        VisitHistoryInfoResponse response = VisitHistoryInfoResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isFalse();
    }

}
