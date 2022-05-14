package com.depromeet.threedollar.domain.rds.user.domain.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StoreStatus {

    ACTIVE("활성화 상태", true),
    DELETED("사용자에 의해 삭제된 상태", false),
    FILTERED("관리자에 의해 삭제된 상태", false),
    ;

    private final String description;
    private final boolean isActivated;

    public boolean isDeleted() {
        return !this.isActivated;
    }

}
