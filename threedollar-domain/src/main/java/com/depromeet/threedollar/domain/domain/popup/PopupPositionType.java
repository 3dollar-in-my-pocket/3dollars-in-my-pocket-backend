package com.depromeet.threedollar.domain.domain.popup;

import com.depromeet.threedollar.common.exception.model.ValidationException;
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
