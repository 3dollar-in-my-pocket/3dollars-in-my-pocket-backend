package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StoreType implements EnumModel {

    ROAD("길거리"),
    STORE("매장"),
    CONVENIENCE_STORE("편의점"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
