package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MedalAcquisitionConditionCreator {

    public static MedalAcquisitionCondition create(Medal medal, MedalAcquisitionConditionType conditionType, int count, String description) {
        return MedalAcquisitionCondition.builder()
            .medal(medal)
            .conditionType(conditionType)
            .count(count)
            .description(description)
            .build();
    }

}
