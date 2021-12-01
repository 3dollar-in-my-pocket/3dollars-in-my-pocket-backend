package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MedalAcquisitionConditionType {

    ADD_STORE,
    ADD_REVIEW,
    VISIT_STORE,
    VISIT_NOT_EXISTS_STORE,
    DELETE_STORE,
    ;

}
