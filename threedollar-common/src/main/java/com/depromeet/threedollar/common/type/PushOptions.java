package com.depromeet.threedollar.common.type;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.Getter;

@Getter
public enum PushOptions implements EnumModel {

    PUSH("푸시 알림"),
    BACKGROUND("백그라운드 푸시"),
    ;

    private final String description;

    PushOptions(String description) {
        this.description = description;
    }

    @Override
    public String getKey() {
        return this.name();
    }

}
