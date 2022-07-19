package com.depromeet.threedollar.domain.rds.domain.commonservice.faq;

import com.depromeet.threedollar.common.model.EnumModel;
import com.depromeet.threedollar.common.type.ApplicationType;
import lombok.Getter;

import java.util.List;

@Getter
public enum FaqCategory implements EnumModel {

    STORE(List.of(ApplicationType.USER_API), "가게", 1),
    REVIEW_MENU(List.of(ApplicationType.USER_API), "리뷰 및 메뉴", 2),
    WITHDRAWAL(List.of(ApplicationType.USER_API), "회원탈퇴", 3),
    BOARD(List.of(ApplicationType.USER_API), "게시글 수정 및 삭제", 4),
    CATEGORY(List.of(ApplicationType.USER_API), "카테고리", 5),
    SIGNUP(List.of(ApplicationType.BOSS_API), "가입", 6),
    MONTHLY_FEE(List.of(ApplicationType.BOSS_API), "월 이용료", 7),
    ETC(List.of(ApplicationType.USER_API, ApplicationType.BOSS_API), "기타", 8),
    ;

    private final List<ApplicationType> supportedApplicationTypes;
    private final String description;
    private final int displayOrder;

    FaqCategory(List<ApplicationType> supportedApplicationTypes, String description, int displayOrder) {
        this.supportedApplicationTypes = supportedApplicationTypes;
        this.description = description;
        this.displayOrder = displayOrder;
    }

    @Override
    public String getKey() {
        return name();
    }

    public boolean isSupported(ApplicationType applicationType) {
        return this.supportedApplicationTypes.contains(applicationType);
    }

}
