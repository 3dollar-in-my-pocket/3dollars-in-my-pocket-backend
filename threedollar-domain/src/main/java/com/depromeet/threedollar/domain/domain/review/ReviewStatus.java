package com.depromeet.threedollar.domain.domain.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReviewStatus {

    POSTED(true),
    FILTERED(false),
    DELETED(false);

    private final boolean isVisible;

}
