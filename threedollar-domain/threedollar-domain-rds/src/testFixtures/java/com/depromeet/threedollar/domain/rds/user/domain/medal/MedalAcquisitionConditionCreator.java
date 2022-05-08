package com.depromeet.threedollar.domain.rds.user.domain.medal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class MedalAcquisitionConditionCreator {

    @Builder
    public static MedalAcquisitionCondition create(
        @NotNull Medal medal,
        @NotNull MedalAcquisitionConditionType conditionType,
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
