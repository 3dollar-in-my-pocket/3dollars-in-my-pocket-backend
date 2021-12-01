package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedalCreator {

    public static Medal create(String name, String iconUrl) {
        return new Medal(name, iconUrl);
    }
}
