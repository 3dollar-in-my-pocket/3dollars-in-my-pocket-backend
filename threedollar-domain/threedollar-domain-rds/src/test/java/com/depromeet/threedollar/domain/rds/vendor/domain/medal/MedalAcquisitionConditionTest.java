package com.depromeet.threedollar.domain.rds.vendor.domain.medal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        MedalAcquisitionCondition medalAcquisitionCondition = MedalAcquisitionConditionCreator.create(MedalCreator.create("메달 A"), conditionType, countCanObtainMedal, "메달 인증 소개");

        // when
        boolean result = medalAcquisitionCondition.canObtain(conditionType, count);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void 메달_획득_정책이_일치하지_않는경우_메달을_획득할_수_없다() {
        // given
        int count = 2;
        MedalAcquisitionCondition medalAcquisitionCondition = MedalAcquisitionConditionCreator.create(MedalCreator.create("우리동네 보안관"), MedalAcquisitionConditionType.ADD_STORE, count, "메달 인증 소개");

        // when
        boolean result = medalAcquisitionCondition.canObtain(MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, count);

        // then
        assertThat(result).isFalse();
    }

}
