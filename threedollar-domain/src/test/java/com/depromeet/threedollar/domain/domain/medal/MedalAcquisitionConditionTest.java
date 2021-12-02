package com.depromeet.threedollar.domain.domain.medal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MedalAcquisitionConditionTest {

    @CsvSource({
        "4, false",
        "5, true",
        "6, true",
    })
    @ParameterizedTest
    void 메달_획득을_위한_활동이_해당_조건을_넘어서야만_메달을_획득할수있다(int count, boolean expectedResult) {
        // given
        int countCanObtainMedal = 5;
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_STORE;
        MedalAcquisitionCondition medalAcquisitionCondition = MedalAcquisitionCondition.of(MedalCreator.create("메달 A", "iconUrl"), conditionType, countCanObtainMedal);

        // when
        boolean result = medalAcquisitionCondition.canObtain(conditionType, count);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void 메달_획득_정책이_일치하지_않는경우_메달을_획득할_수_없다() {
        // given
        int count = 2;
        MedalAcquisitionCondition medalAcquisitionCondition = MedalAcquisitionCondition.of(MedalCreator.create("메달 A", "iconUrl"), MedalAcquisitionConditionType.ADD_STORE, count);

        // when
        boolean result = medalAcquisitionCondition.canObtain(MedalAcquisitionConditionType.VISIT_STORE, count);

        // then
        assertThat(result).isFalse();
    }

}
