package com.depromeet.threedollar.domain.rds.user.domain.advertisement;

import com.depromeet.threedollar.common.model.EnumModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AdvertisementPositionType implements EnumModel {

    SPLASH("스플래시"),
    MAIN_PAGE_CARD("메인 페이지 가게 카드"),
    STORE_CATEGORY_LIST("가게 카테고리"),
    MENU_CATEGORY_BANNER("메뉴 카테고리 배너"),
    ;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
