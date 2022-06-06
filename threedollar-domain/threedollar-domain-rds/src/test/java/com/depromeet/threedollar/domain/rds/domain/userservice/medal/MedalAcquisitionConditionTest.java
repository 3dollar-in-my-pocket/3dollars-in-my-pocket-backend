package com.depromeet.threedollar.domain.rds.domain.userservice.medal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MedalAcquisitionConditionTest {

    @CsvSource({
        "1, false",
        "2, true",
        "3, true",
    })
    @ParameterizedTest
    void 메달_획득을_위한_활동이_해당_조건을_넘어서야만_메달을_획득할수있다(int count, boolean expectedResult) {
        // given
        int countCanObtainMedal = 2;
        MedalAcquisitionConditionType conditionType = MedalAcquisitionConditionType.ADD_STORE;
        Medal medal = MedalCreator.create("메달 A");
        MedalAcquisitionCondition medalAcquisitionCondition = MedalAcquisitionConditionCreator.create(medal, conditionType, countCanObtainMedal, "메달 인증 소개");

        // when
        boolean result = medalAcquisitionCondition.canObtain(conditionType, count);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void 해당_메달_타입에_해당하는_조건을_만족해야만_메달을_획득할_수_있다() {
        // given
        int count = 1;
        Medal medal = MedalCreator.create("우리동네 보안관");
        MedalAcquisitionCondition medalAcquisitionCondition = MedalAcquisitionConditionCreator.create(medal, MedalAcquisitionConditionType.ADD_STORE, count, "메달 인증 소개");

        // when
        boolean result = medalAcquisitionCondition.canObtain(MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, count);

        // then
        assertThat(result).isFalse();
    }

}
