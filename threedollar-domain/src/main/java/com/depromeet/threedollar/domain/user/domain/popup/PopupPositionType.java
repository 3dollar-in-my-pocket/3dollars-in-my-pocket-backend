package com.depromeet.threedollar.domain.user.domain.popup;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PopupPositionType implements EnumModel {

    SPLASH("스플래시"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
