package com.depromeet.threedollar.common.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
enum FileContentType {

    IMAGE("image"),
    ;

    private final String prefix;

}
