package com.depromeet.threedollar.domain.user.domain.faq;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FaqCategory {

    STORE("가게", 1),
    REVIEW_MENU("리뷰 및 메뉴", 2),
    WITHDRAWAL("회원탈퇴", 3),
    BOARD("게시글 수정 및 삭제", 4),
    CATEGORY("카테고리", 5),
    ETC("기타", 6),
    ;

    private final String description;
    private final int displayOrder;

}
