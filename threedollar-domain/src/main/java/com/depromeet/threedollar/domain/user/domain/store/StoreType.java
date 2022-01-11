package com.depromeet.threedollar.domain.user.domain.store;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StoreType {

    ROAD("길거리"),
    STORE("매장"),
    CONVENIENCE_STORE("편의점"),
    ;

    private final String description;

}
