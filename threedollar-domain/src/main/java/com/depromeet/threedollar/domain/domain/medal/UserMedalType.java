package com.depromeet.threedollar.domain.domain.medal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserMedalType {

    BUNGEOPPANG_CHALLENGER("붕어빵 챌린저", 1, 0, 0, 0, 0),
    BUNGEOPPANG_EXPERT("붕어빵 전문가", 3, 0, 0, 0, 0),
    BASTARD_IN_THIS_AREA("이 구역 붕친놈은 나야", 10, 0, 0, 0, 0),
    IM_NOT_EVEN_AN_IDIOT("내가 각설이도 아니고", 0, 5, 0, 0, 0),
    IM_THE_MICHELIN_STA("미슐랭 평가단이 바로 나에요", 0, 0, 5, 0, 0),
    SERVE_THIS_PERSON("사장님, 이 분 서비스 주세요", 0, 0, 0, 3, 0),
    MY_NEIGHBORHOOD_SHERIFF("우리 동네 보안관", 0, 0, 0, 0, 3),
    ;

    private final String description;
    private final long visitHistoriesCounts;
    private final long notExistsVisitHistoriesCounts;
    private final long reviewsCounts;
    private final long addStoresCounts;
    private final long deleteStoreRequestsCounts;

    public boolean isMatched(long visitHistoriesCounts, long notExistsVisitHistoriesCounts, long reviewsCounts, long addStoresCounts, long deleteRequestCounts) {
        return this.visitHistoriesCounts <= visitHistoriesCounts
            && this.notExistsVisitHistoriesCounts <= notExistsVisitHistoriesCounts
            && this.reviewsCounts <= reviewsCounts
            && this.addStoresCounts <= addStoresCounts
            && this.deleteStoreRequestsCounts <= deleteRequestCounts;
    }

}
