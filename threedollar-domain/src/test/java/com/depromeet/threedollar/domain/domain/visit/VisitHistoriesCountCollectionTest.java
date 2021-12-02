package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryCountProjection;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class VisitHistoriesCountCollectionTest {

    @AutoSource
    @ParameterizedTest
    void 가게별로_방문_성공_실패_카운트를_저장하는_컬렉션(Long storeOneId, Long storeTwoId) {
        // given
        VisitHistoryCountProjection storeOneExistsCounts = new VisitHistoryCountProjection(storeOneId, VisitType.EXISTS, 3);
        VisitHistoryCountProjection storeOneNotExistsCounts = new VisitHistoryCountProjection(storeOneId, VisitType.NOT_EXISTS, 1);

        VisitHistoryCountProjection storeTwoExistsCounts = new VisitHistoryCountProjection(storeTwoId, VisitType.EXISTS, 2);
        VisitHistoryCountProjection storeTwoNotExistsCounts = new VisitHistoryCountProjection(storeTwoId, VisitType.NOT_EXISTS, 0);

        // when
        VisitHistoriesCounterCollection collection = VisitHistoriesCounterCollection.of(
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
