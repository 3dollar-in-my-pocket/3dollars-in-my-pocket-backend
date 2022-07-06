package com.depromeet.threedollar.push.infra.firebase;

import lombok.Getter;

@Getter
public enum FirebaseAppType {

    USER("USER_APP"),
    BOSS("BOSS_APP"),
    ;

    private final String appName;

    FirebaseAppType(String appName) {
        this.appName = appName;
    }

}
