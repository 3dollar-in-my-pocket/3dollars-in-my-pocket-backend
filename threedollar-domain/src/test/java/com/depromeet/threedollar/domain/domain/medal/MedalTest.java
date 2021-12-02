package com.depromeet.threedollar.domain.domain.medal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MedalTest {

    @CsvSource({
        "4, false",
        "5, true",
        "6, true",
    })
    @ParameterizedTest
    void 메달에_대한_획득_조건을_만족하는지_확인한다(int count, boolean expectedResult) {
        // given
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_STORE;
        Medal medal = MedalCreator.create("메달 A", "iconUrl", conditionType, 5);

        // whe
        boolean result = medal.canObtain(MedalAcquisitionConditionType.ADD_STORE, count);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void 메달_획득_조건이_일치하지_않는경우_return_false() {
        // given
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_STORE;
        Medal medal = MedalCreator.create("메달 A", "iconUrl", conditionType, 2);

        // whe
        boolean result = medal.canObtain(MedalAcquisitionConditionType.VISIT_STORE, 2);

        // then
        assertThat(result).isFalse();
    }

}
