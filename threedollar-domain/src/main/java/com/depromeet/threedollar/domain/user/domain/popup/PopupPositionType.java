package com.depromeet.threedollar.domain.user.domain.popup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PopupPositionType {

    SPLASH("스플래시"),
    ;

    private final String description;

}
