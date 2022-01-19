package com.depromeet.threedollar.domain.user.domain.popup;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PopupPlatformType implements EnumModel {

    AOS("안드로이드"),
    IOS("iOS"),
    ;

    private final String description;


    @Override
    public String getKey() {
        return name();
    }

}
