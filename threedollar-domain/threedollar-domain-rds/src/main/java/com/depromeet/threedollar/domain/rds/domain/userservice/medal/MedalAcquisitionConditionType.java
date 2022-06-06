package com.depromeet.threedollar.domain.rds.domain.userservice.medal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MedalAcquisitionConditionType {

    NO_CONDITION("기본 메달"),
    ADD_STORE("가게 등록"),
    ADD_REVIEW("리뷰 등록"),
    VISIT_BUNGEOPPANG_STORE("붕어빵 가게 방문 인증"),
    VISIT_NOT_EXISTS_STORE("존재하지 않는 가게 방문 인증"),
    DELETE_STORE("가게 삭제 요청"),
    ;

    private final String description;

}
