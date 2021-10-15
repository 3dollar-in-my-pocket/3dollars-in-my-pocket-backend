package com.depromeet.threedollar.domain.domain.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.domain.domain.menu.MenuCategoryType.MenuCategoryTypeStatus.ACTIVE;

@Getter
@RequiredArgsConstructor
public enum MenuCategoryType {

    DALGONA("달고나", true, ACTIVE, 1),
    BUNGEOPPANG("붕어빵", false, ACTIVE, 2),
    HOTTEOK("호떡", false, ACTIVE, 3),
    TAKOYAKI("타코야끼", false, ACTIVE, 4),
    GYERANPPANG("계란빵", false, ACTIVE, 5),
    SUNDAE("순대", false, ACTIVE, 6),
    EOMUK("어묵", false, ACTIVE, 7),
    TTEOKBOKKI("떡볶이", false, ACTIVE, 8),
    TTANGKONGPPANG("땅콩빵", false, ACTIVE, 9),
    KKOCHI("꼬치", false, ACTIVE, 10),
    WAFFLE("와플", false, ACTIVE, 11),
    GUKWAPPANG("국화빵", false, ACTIVE, 12),
    TOAST("토스트", false, ACTIVE, 13),
    GUNGOGUMA("군고구마", false, ACTIVE, 14),
    GUNOKSUSU("군옥수수", false, ACTIVE, 15),
    ;

    private final String name;
    private final boolean isNew;
    private final MenuCategoryTypeStatus status;
    private final int displayOrder;

    public boolean isViewed() {
        return this.status.isViewed;
    }

    @RequiredArgsConstructor
    public enum MenuCategoryTypeStatus {
        ACTIVE(true),
        INACTIVE(false),
        ;

        private final boolean isViewed;
    }

}
