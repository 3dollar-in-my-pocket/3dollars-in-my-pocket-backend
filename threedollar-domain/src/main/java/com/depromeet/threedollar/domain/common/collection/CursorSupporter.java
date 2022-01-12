package com.depromeet.threedollar.domain.common.collection;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 커서 기반 페이지네이션을 위한 컬렉션
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorSupporter<T> {

    private final List<T> itemsWithNextCursor; // 현재 스크롤의 요소 + 다음 스크롤의 요소 1개 (다음 스크롤이 있는지 확인을 위한)
    private final int countsPerCursor;

    public static <T> CursorSupporter<T> of(List<T> itemsWithNextCursor, int size) {
        return new CursorSupporter<>(itemsWithNextCursor, size);
    }

    public boolean isLastCursor() {
        return this.itemsWithNextCursor.size() <= countsPerCursor;
    }

    public List<T> getItemsInCurrentCursor() {
        if (isLastCursor()) {
            return this.itemsWithNextCursor;
        }
        return this.itemsWithNextCursor.subList(0, countsPerCursor);
    }

    @NotNull
    public T getNextCursor() {
        return itemsWithNextCursor.get(countsPerCursor - 1);
    }

}
