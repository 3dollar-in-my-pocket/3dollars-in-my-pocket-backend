package com.depromeet.threedollar.domain.user.domain.storedelete;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeleteReasonType {

    NOSTORE("없어진 가게에요"),
    WRONGNOPOSITION("위치가 잘못됐어요"),
    OVERLAPSTORE("중복 제보된 가게에요"),
    WRONG_CONTENT("부적절한 내용이 있어요"),
    ;

    private final String description;

}
