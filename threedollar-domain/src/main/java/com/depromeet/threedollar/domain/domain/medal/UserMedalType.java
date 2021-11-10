package com.depromeet.threedollar.domain.domain.medal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserMedalType {

    BUNGEOPPANG_CHALLENGER("붕어빵 챌린저"),
    ;

    private final String description;

}
