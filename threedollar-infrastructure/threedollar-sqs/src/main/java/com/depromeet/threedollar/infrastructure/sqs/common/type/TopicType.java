package com.depromeet.threedollar.infrastructure.sqs.common.type;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TopicType implements EnumModel {

    BOSS_SINGLE_APP_PUSH("BOSS_SINGLE_APP_PUSH", "(사장님 서비스) 단건 푸시 알림"),
    BOSS_BULK_APP_PUSH("BOSS_BULK_APP_PUSH", "(사장님 서비스) 벌크 푸시 알림"),
    ;

    private final String code;
    private final String description;

    @Override
    public String getKey() {
        return this.name();
    }

}
