package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.domain.domain.medal.UserMedalType.MedalAcquisitionCondition.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserMedalType {

    BUNGEOPPANG_CHALLENGER("붕어빵 챌린저", 1, VISIT_STORE),
    BUNGEOPPANG_EXPERT("붕어빵 전문가", 3, VISIT_STORE),
    BASTARD_IN_THIS_AREA("이 구역 붕친놈은 나야", 10, VISIT_STORE),
    IM_NOT_EVEN_AN_IDIOT("내가 각설이도 아니고", 5, VISIT_NOT_EXISTS_STORE),
    IM_THE_MICHELIN_STA("미슐랭 평가단이 바로 나에요", 5, ADD_REVIEW),
    PLEASE_SERVER_THIS_PERSON("사장님, 이 분 서비스 주세요", 3, ADD_STORE),
    MY_NEIGHBORHOOD_SHERIFF("우리 동네 보안관", 3, DELETE_STORE),
    ;

    private final String description;
    private final long counts;
    private final MedalAcquisitionCondition condition;

    public boolean isMatchCondition(MedalAcquisitionCondition condition) {
        return this.condition.equals(condition);
    }

    public boolean canObtain(long counts) {
        return this.counts <= counts;
    }

    public enum MedalAcquisitionCondition {
        ADD_STORE,
        ADD_REVIEW,
        VISIT_STORE,
        VISIT_NOT_EXISTS_STORE,
        DELETE_STORE
    }

}
