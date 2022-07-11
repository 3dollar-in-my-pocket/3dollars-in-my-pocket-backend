package com.depromeet.threedollar.common.type;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.Getter;

@Getter
public enum PushOptionsType implements EnumModel {

    PUSH("푸시 알림"),
    BACKGROUND("백그라운드 푸시"),
    ;

    private final String description;

    PushOptionsType(String description) {
        this.description = description;
    }

    @Override
    public String getKey() {
        return this.name();
    }

}
