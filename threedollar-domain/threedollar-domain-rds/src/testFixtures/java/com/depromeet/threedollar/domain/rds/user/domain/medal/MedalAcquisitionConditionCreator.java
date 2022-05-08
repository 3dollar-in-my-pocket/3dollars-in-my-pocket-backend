package com.depromeet.threedollar.domain.rds.user.domain.medal;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.Builder;

@TestFixture
public class MedalAcquisitionConditionCreator {

    @Builder
    public static MedalAcquisitionCondition create(
        Medal medal,
        MedalAcquisitionConditionType conditionType,
        int count,
        @Nullable String description
    ) {
        return MedalAcquisitionCondition.builder()
            .medal(medal)
            .conditionType(conditionType)
            .count(count)
            .description(description)
            .build();
    }

}
