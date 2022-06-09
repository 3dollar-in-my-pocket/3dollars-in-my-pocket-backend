package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.common.model.EnumModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DeleteReasonType implements EnumModel {

    NOSTORE("없어진 가게에요"),
    WRONGNOPOSITION("위치가 잘못됐어요"),
    OVERLAPSTORE("중복 제보된 가게에요"),
    WRONG_CONTENT("부적절한 내용이 있어요"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
