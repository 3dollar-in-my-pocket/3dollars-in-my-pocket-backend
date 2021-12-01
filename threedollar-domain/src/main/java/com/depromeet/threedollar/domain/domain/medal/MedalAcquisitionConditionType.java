package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MedalAcquisitionConditionType {

    ADD_STORE("가게 등록"),
    ADD_REVIEW("리뷰 등록"),
    VISIT_STORE("가게 방문 인증"),
    VISIT_NOT_EXISTS_STORE("존재하지 않는 가게 방문 인증"),
    DELETE_STORE("가게 삭제 요청"),
    ;

    private final String description;

}
