package com.depromeet.threedollar.domain.user.domain.medal;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedalCreator {

    public static Medal create(String name) {
        return Medal.builder()
            .name(name)
            .introduction("메달 설명")
            .activationIconUrl("https://activation-icon-url.png")
            .disableIconUrl("https://disableIconurl.png")
            .conditionType(MedalAcquisitionConditionType.ADD_STORE)
            .count(3)
            .build();
    }

    public static Medal create(String name, MedalAcquisitionConditionType conditionType, int count) {
        return Medal.builder()
            .name(name)
            .introduction("메달 설명")
            .activationIconUrl("iconUrl")
            .disableIconUrl("disableUrl")
            .conditionType(conditionType)
            .count(count)
            .build();
    }

    public static Medal create(String name, String introduction, String activationIconUrl, String disableIconUrl) {
        return Medal.builder()
            .name(name)
            .introduction(introduction)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .conditionType(MedalAcquisitionConditionType.ADD_STORE)
            .count(3)
            .build();
    }

    public static Medal create(String name, String introduction, String activationIconUrl, String disableIconUrl, MedalAcquisitionConditionType conditionType, int count) {
        return Medal.builder()
            .name(name)
            .introduction(introduction)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .conditionType(conditionType)
            .count(count)
            .build();
    }

}
