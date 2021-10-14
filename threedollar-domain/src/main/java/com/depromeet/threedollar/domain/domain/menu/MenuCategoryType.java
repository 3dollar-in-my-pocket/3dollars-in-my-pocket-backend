package com.depromeet.threedollar.domain.domain.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategoryType {

    BUNGEOPPANG("붕어빵"),
    TAKOYAKI("타코야끼"),
    GYERANPPANG("계란빵"),
    HOTTEOK("호떡"),
    EOMUK("어묵"),
    GUNGOGUMA("군고구마"),
    TTEOKBOKKI("떡볶이"),
    TTANGKONGPPANG("땅콩빵"),
    GUNOKSUSU("군옥수수"),
    KKOCHI("꼬치"),
    TOAST("토스트"),
    WAFFLE("와플"),
    GUKWAPPANG("국화빵"),
    SUNDAE("순대"),
    DALGONA("달고나")
    ;

    private final String description;

}
