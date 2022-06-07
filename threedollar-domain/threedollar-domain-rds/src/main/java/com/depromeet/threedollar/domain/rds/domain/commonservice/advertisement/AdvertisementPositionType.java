package com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement;

import java.util.List;

import com.depromeet.threedollar.common.model.EnumModel;
import com.depromeet.threedollar.common.type.ApplicationType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AdvertisementPositionType implements EnumModel {

    SPLASH(List.of(ApplicationType.USER_API), "스플래시"),
    MAIN_PAGE_CARD(List.of(ApplicationType.USER_API), "메인 페이지 가게 카드"),
    STORE_CATEGORY_LIST(List.of(ApplicationType.USER_API), "가게 카테고리"),
    MENU_CATEGORY_BANNER(List.of(ApplicationType.USER_API), "메뉴 카테고리 배너"),
    ;

    private final List<ApplicationType> supportedApplicationTypes;
    private final String description;

    @Override
    public String getKey() {
        return name();
    }

    public boolean isSupported(ApplicationType applicationType) {
        return this.supportedApplicationTypes.contains(applicationType);
    }

}
