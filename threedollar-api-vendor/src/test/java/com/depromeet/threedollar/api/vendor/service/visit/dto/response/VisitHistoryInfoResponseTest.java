package com.depromeet.threedollar.api.vendor.service.visit.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VisitHistoryInfoResponseTest {

    @Test
    void 방문_인증한_사람이_방문하지_않은_사람보다_많은경우_인증된_가게로_표시된다() {
        // given
        int existsVisitCount = 2;
        int notExistsVisitsCount = 1;

        // when
        VisitHistoryCountsResponse response = VisitHistoryCountsResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isTrue();
    }

    @CsvSource({
        "1, 2",
        "0, 0"
    })
    @ParameterizedTest
    void 방문_인증한_사람이_방문하지_않은_사람보다_같거나_적은경우_인증되지_않은_가게로_표시된다(int existsVisitCount, int notExistsVisitsCount) {
        // when
        VisitHistoryCountsResponse response = VisitHistoryCountsResponse.of(existsVisitCount, notExistsVisitsCount);

        // then
        assertThat(response.getIsCertified()).isFalse();
    }

}
