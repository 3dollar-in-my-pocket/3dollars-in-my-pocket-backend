package com.depromeet.threedollar.common.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class EnumValue {

    private final String key;
    private final String description;

    private EnumValue(EnumModel enumModel) {
        key = enumModel.getKey();
        description = enumModel.getDescription();
    }

    public static EnumValue of(EnumModel enumModel) {
        return new EnumValue(enumModel);
    }

}
