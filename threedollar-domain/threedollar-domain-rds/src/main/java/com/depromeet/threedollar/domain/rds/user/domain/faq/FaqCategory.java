package com.depromeet.threedollar.domain.rds.user.domain.faq;

import com.depromeet.threedollar.common.model.EnumModel;

import lombok.Getter;

@Getter
public enum FaqCategory implements EnumModel {

    STORE("가게", 1),
    REVIEW_MENU("리뷰 및 메뉴", 2),
    WITHDRAWAL("회원탈퇴", 3),
    BOARD("게시글 수정 및 삭제", 4),
    CATEGORY("카테고리", 5),
    ETC("기타", 6),
    ;

    private final String description;
    private final int displayOrder;

    FaqCategory(String description, int displayOrder) {
        this.description = description;
        this.displayOrder = displayOrder;
    }

    @Override
    public String getKey() {
        return name();
    }

}
