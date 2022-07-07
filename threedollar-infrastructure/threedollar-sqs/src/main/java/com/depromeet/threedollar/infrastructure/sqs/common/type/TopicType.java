package com.depromeet.threedollar.infrastructure.sqs.common.type;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TopicType implements EnumModel {

    SINGLE_APP_PUSH("SINGLE_PUSH", "단건 푸시 알림"),
    BULK_APP_PUSH("BULK_PUSH", "다건 푸시 알림"),
    ;

    private final String code;
    private final String description;

    @Override
    public String getKey() {
        return this.name();
    }

}
