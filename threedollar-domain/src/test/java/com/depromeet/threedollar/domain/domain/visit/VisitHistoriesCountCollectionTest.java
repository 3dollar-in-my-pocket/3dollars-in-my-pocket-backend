package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithCounts;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class VisitHistoriesCountCollectionTest {

    @Test
    void 가게별로_방문_성공_실패_카운트를_저장하는_컬렉션() {
        // given
        Long storeOneId = 10000L;
        VisitHistoryWithCounts storeOneExistsCounts = new VisitHistoryWithCounts(storeOneId, VisitType.EXISTS, 3);
        VisitHistoryWithCounts storeOneNotExistsCounts = new VisitHistoryWithCounts(storeOneId, VisitType.NOT_EXISTS, 1);

        Long storeTwoId = 10001L;
        VisitHistoryWithCounts storeTwoExistsCounts = new VisitHistoryWithCounts(storeTwoId, VisitType.EXISTS, 2);
        VisitHistoryWithCounts storeTwoNotExistsCounts = new VisitHistoryWithCounts(storeTwoId, VisitType.NOT_EXISTS, 0);

        // when
        VisitHistoriesCountCollection collection = VisitHistoriesCountCollection.of(
            List.of(storeOneExistsCounts, storeOneNotExistsCounts, storeTwoExistsCounts, storeTwoNotExistsCounts));

        // then
        assertAll(
            () -> assertThat(collection.getStoreExistsVisitsCount(storeOneId)).isEqualTo(3L),
            () -> assertThat(collection.getStoreNotExistsVisitsCount(storeOneId)).isEqualTo(1L),

            () -> assertThat(collection.getStoreExistsVisitsCount(storeTwoId)).isEqualTo(2L),
            () -> assertThat(collection.getStoreNotExistsVisitsCount(storeTwoId)).isEqualTo(0L)
        );
    }

}
