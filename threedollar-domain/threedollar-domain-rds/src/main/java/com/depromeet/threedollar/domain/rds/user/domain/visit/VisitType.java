package com.depromeet.threedollar.domain.rds.user.domain.visit;

import com.depromeet.threedollar.common.model.EnumModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VisitType implements EnumModel {

    EXISTS("존재하는 가게"),
    NOT_EXISTS("존재하지 않는 가게"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
