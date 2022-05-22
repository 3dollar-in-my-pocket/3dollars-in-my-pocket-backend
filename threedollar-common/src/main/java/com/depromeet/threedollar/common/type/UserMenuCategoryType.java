package com.depromeet.threedollar.common.type;

import static com.depromeet.threedollar.common.type.UserMenuCategoryType.MenuCategoryTypeStatus.ACTIVE;

import com.depromeet.threedollar.common.model.EnumModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserMenuCategoryType implements EnumModel {

    BUNGEOPPANG("붕어빵", "붕어빵 만나기 30초 전", false, ACTIVE, 1),
    HOTTEOK("호떡", "호떡아 기다려", false, ACTIVE, 2),
    TAKOYAKI("문어빵", "문어빵 다 내꺼야", false, ACTIVE, 3),
    EOMUK("어묵", "날 쏴줘 어묵 탕!", false, ACTIVE, 4),
    GYERANPPANG("계란빵", "계란빵, 내 입으로", false, ACTIVE, 5),
    TTEOKBOKKI("떡볶이", "떡볶이...\n너 500원이었잖아", false, ACTIVE, 6),
    SUNDAE("순대", "순대, 제발 내장 많이 주세요", false, ACTIVE, 7),
    WAFFLE("와플", "넌 어쩜 이름도\n와플일까?", false, ACTIVE, 8),
    KKOCHI("꼬치", "꼬치꼬치 캐묻지마 ♥", false, ACTIVE, 9),
    TTANGKONGPPANG("땅콩빵", "땅콩빵, 오늘 널 갖겠어", false, ACTIVE, 10),
    GUKWAPPANG("국화빵", "사계절 너가 생각나\n국화빵", false, ACTIVE, 11),
    GUNGOGUMA("군고구마", "널 생각하면 목이막혀,\n군고구마", false, ACTIVE, 12),
    TOAST("토스트", "너네 사이에 나도 껴주라,\n토스트", false, ACTIVE, 13),
    DALGONA("달고나", "456번째 달고나", false, ACTIVE, 14),
    GUNOKSUSU("군옥수수", "버터까지 발라서 굽겠어\n군옥수수", false, ACTIVE, 15),
    ;

    private final String categoryName;
    private final String description;
    private final boolean isNew;
    private final MenuCategoryTypeStatus status;
    private final int displayOrder;

    public boolean isVisible() {
        return this.status.isVisible;
    }

    @Override
    public String getKey() {
        return name();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum MenuCategoryTypeStatus {
        ACTIVE(true),
        INACTIVE(false),
        ;

        private final boolean isVisible;
    }

}
