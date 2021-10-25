package com.depromeet.threedollar.domain.domain.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.domain.domain.menu.MenuCategoryType.MenuCategoryTypeStatus.ACTIVE;

@Getter
@RequiredArgsConstructor
public enum MenuCategoryType {

    DALGONA("달고나", "456번째 달고나", true, ACTIVE, 1),
    BUNGEOPPANG("붕어빵", "붕어빵 만나기 30초 전", false, ACTIVE, 2),
    HOTTEOK("호떡", "호떡아 기다려", false, ACTIVE, 3),
    TAKOYAKI("문어빵", "문어빵 다 내꺼야", false, ACTIVE, 4),
    GYERANPPANG("계란빵", "계란빵, 내 입으로", false, ACTIVE, 5),
    EOMUK("어묵", "날 쏴줘 어묵 탕!", false, ACTIVE, 6),
    TTEOKBOKKI("떡볶이", "떡볶이...\n너 500원이었잖아", false, ACTIVE, 7),
    SUNDAE("순대", "순대, 제발 내장 많이 주세요", false, ACTIVE, 8),
    WAFFLE("와플", "넌 어쩜 이름도\n와플일까?", false, ACTIVE, 9),
    KKOCHI("꼬치", "꼬치꼬치 캐묻지마 ♥", false, ACTIVE, 10),
    TTANGKONGPPANG("땅콩빵", "땅콩빵, 오늘 널 갖겠어", false, ACTIVE, 11),
    GUKWAPPANG("국화빵", "사계절 너가 생각나\n국화빵", false, ACTIVE, 12),
    GUNGOGUMA("군고구마", "널 생각하면 목이막혀,\n군고구마", false, ACTIVE, 13),
    TOAST("토스트", "너네 사이에 나도 껴주라,\n토스트", false, ACTIVE, 14),
    GUNOKSUSU("군옥수수", "버터까지 발라서 굽겠어\n군옥수수", false, ACTIVE, 15),
    ;

    private final String categoryName;
    private final String description;
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
