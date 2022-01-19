package com.depromeet.threedollar.common.model;

import lombok.Getter;

@Getter
public class EnumValue {

    private final String key;

    private final String description;

    public EnumValue(EnumModel enumModel) {
        key = enumModel.getKey();
        description = enumModel.getDescription();
    }

}
