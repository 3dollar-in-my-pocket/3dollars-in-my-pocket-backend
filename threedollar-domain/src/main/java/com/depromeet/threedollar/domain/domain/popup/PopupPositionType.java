package com.depromeet.threedollar.domain.domain.popup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PopupPositionType {

    BANNER("배너"),
    ;

    private final String description;

}
