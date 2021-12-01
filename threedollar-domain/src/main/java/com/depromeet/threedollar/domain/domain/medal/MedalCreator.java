package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedalCreator {

    public static Medal create(String name, String iconUrl) {
        return new Medal(name, iconUrl, MedalAcquisitionConditionType.ADD_STORE, 3);
    }

    public static Medal create(String name, String iconUrl, MedalAcquisitionConditionType conditionType, int count) {
        return new Medal(name, iconUrl, conditionType, count);
    }

}
