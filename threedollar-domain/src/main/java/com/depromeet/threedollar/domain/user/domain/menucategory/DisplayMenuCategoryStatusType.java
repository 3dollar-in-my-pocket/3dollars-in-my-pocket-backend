package com.depromeet.threedollar.domain.user.domain.menucategory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DisplayMenuCategoryStatusType {

    ACTIVE(true),
    INACTIVE(false),
    ;

    private final boolean isVisible;

}
