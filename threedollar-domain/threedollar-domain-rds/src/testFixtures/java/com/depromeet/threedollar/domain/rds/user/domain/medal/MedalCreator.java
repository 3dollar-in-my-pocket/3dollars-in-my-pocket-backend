package com.depromeet.threedollar.domain.rds.user.domain.medal;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        return Medal.builder()
            .name(name)
            .introduction(Optional.ofNullable(introduction).orElse("메달 설명"))
            .activationIconUrl(Optional.ofNullable(activationIconUrl).orElse("https://activation-iconUrl.png"))
            .disableIconUrl(Optional.ofNullable(disableIconUrl).orElse("https://disable-iconUrl.png"))
            .conditionType(Optional.ofNullable(conditionType).orElse(MedalAcquisitionConditionType.ADD_STORE))
            .conditionCount(Optional.ofNullable(conditionCount).orElse(3))
            .acquisitionDescription(acquisitionDescription)
            .build();
    }

}
