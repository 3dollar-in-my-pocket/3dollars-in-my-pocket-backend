package com.depromeet.threedollar.common.type;

import lombok.Getter;

@Getter
public enum ApplicationType {

    USER_API("유저 API 서버"),
    BOSS_API("사장님 API 서버"),
    ADMIN_API("관리자 서버"),
    BATCH("배치 서버"),
    ;

    private ApplicationType(String description) {
        this.description = description;
    }

    private final String description;

}
