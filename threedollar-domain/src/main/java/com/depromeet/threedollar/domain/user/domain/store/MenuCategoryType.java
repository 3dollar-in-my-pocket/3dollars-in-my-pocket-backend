package com.depromeet.threedollar.domain.user.domain.store;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MenuCategoryType {

    BUNGEOPPANG("붕어빵"),
    HOTTEOK("호떡"),
    TAKOYAKI("문어빵"),
    EOMUK("어묵"),
    GYERANPPANG("계란빵"),
    TTEOKBOKKI("떡볶이"),
    SUNDAE("순대"),
    WAFFLE("와플"),
    KKOCHI("꼬치"),
    TTANGKONGPPANG("땅콩빵"),
    GUKWAPPANG("국화빵"),
    GUNGOGUMA("군고구마"),
    TOAST("토스트"),
    DALGONA("달고나"),
    GUNOKSUSU("군옥수수"),
    ;

    private final String categoryName;

}
