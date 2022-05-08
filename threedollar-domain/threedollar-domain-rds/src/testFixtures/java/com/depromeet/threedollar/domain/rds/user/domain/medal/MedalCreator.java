package com.depromeet.threedollar.domain.rds.user.domain.medal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class MedalCreator {

    @Builder
    public static Medal create(
        @NotNull String name,
        @Nullable String introduction,
        @Nullable String activationIconUrl,
        @Nullable String disableIconUrl,
        @Nullable MedalAcquisitionConditionType conditionType,
        @Nullable Integer conditionCount,
        @Nullable String acquisitionDescription
    ) {
        if (!StringUtils.hasText(introduction)) {
            introduction = "메달 설명";
        }
        if (!StringUtils.hasText(activationIconUrl)) {
            activationIconUrl = "iconUrl";
        }
        if (!StringUtils.hasText(disableIconUrl)) {
            disableIconUrl = "disableUrl";
        }
        if (conditionType == null) {
            conditionType = MedalAcquisitionConditionType.ADD_STORE;
        }
        if (conditionCount == null) {
            conditionCount = 3;
        }

        return Medal.builder()
            .name(name)
            .introduction(introduction)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .conditionType(conditionType)
            .conditionCount(conditionCount)
            .acquisitionDescription(acquisitionDescription)
            .build();
    }

}
