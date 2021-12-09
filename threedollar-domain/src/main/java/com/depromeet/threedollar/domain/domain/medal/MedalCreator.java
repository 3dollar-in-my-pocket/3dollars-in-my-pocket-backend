package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedalCreator {

    public static Medal create(String name, String activationIconUrl, String disableIconUrl) {
        return new Medal(name, activationIconUrl, disableIconUrl, MedalAcquisitionConditionType.ADD_STORE, 3);
    }

    public static Medal create(String name, String iconUrl, MedalAcquisitionConditionType conditionType, int count) {
        return new Medal(name, iconUrl, "disableIconUrl", conditionType, count);
    }

}
