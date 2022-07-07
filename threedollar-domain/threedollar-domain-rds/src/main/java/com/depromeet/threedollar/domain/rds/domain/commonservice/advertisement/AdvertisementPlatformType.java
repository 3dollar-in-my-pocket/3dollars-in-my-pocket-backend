package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement;

import com.depromeet.threedollar.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AdvertisementPlatformType implements EnumModel {

    AOS("안드로이드"),
    IOS("iOS"),
    ALL("모든 플랫폼"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
