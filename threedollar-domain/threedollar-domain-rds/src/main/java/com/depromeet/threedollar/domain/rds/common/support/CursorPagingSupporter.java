package com.depromeet.threedollar.domain.rds.common.support;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 커서 기반 페이지네이션을 위한 컬렉션
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorPagingSupporter<T> {

    private final List<T> itemsWithNextCursor; // 현재 스크롤의 요소 + 다음 스크롤의 요소 1개 (다음 스크롤이 있는지 확인을 위한)
    private final int sizePerCursor;

    public static <T> CursorPagingSupporter<T> of(List<T> itemsWithNextCursor, int size) {
        return new CursorPagingSupporter<>(itemsWithNextCursor, size);
    }

    public boolean hasNext() {
        return this.itemsWithNextCursor.size() > sizePerCursor;
    }

    public List<T> getCurrentCursorItems() {
        if (hasNext()) {
            return this.itemsWithNextCursor.subList(0, sizePerCursor);
        }
        return this.itemsWithNextCursor;
    }

    @NotNull
    public T getNextCursor() {
        return itemsWithNextCursor.get(sizePerCursor - 1);
    }

}
