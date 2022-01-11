package com.depromeet.threedollar.domain.user.domain.visit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VisitType {

    EXISTS("존재하는 가게"),
    NOT_EXISTS("존재하지 않는 가게"),
    ;

    private final String description;

}
