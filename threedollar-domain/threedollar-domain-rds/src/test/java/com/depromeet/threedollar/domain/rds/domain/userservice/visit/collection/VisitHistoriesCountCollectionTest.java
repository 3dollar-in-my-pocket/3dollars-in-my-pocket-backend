package com.depromeet.threedollar.domain.rds.domain.userservice.visit.collection;

import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryCountProjection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class VisitHistoriesCountCollectionTest {

    @Test
    void 가게별로_방문_성공_및_실패_카운트를_반환한다() {
        // given
        Long storeOneId = 333333L;
        Long storeTwoId = 777777L;

        VisitHistoryCountProjection storeOneExistsCounts = new VisitHistoryCountProjection(storeOneId, VisitType.EXISTS, 3);
        VisitHistoryCountProjection storeOneNotExistsCounts = new VisitHistoryCountProjection(storeOneId, VisitType.NOT_EXISTS, 1);

        VisitHistoryCountProjection storeTwoExistsCounts = new VisitHistoryCountProjection(storeTwoId, VisitType.EXISTS, 2);
        VisitHistoryCountProjection storeTwoNotExistsCounts = new VisitHistoryCountProjection(storeTwoId, VisitType.NOT_EXISTS, 0);

        // when
        VisitHistoryCounter counter = VisitHistoryCounter.of(
            List.of(storeOneExistsCounts, storeOneNotExistsCounts, storeTwoExistsCounts, storeTwoNotExistsCounts));

        // then
        assertAll(
            () -> assertThat(counter.getStoreExistsVisitsCount(storeOneId)).isEqualTo(3L),
            () -> assertThat(counter.getStoreNotExistsVisitsCount(storeOneId)).isEqualTo(1L),

            () -> assertThat(counter.getStoreExistsVisitsCount(storeTwoId)).isEqualTo(2L),
            () -> assertThat(counter.getStoreNotExistsVisitsCount(storeTwoId)).isZero()
        );
    }

}
