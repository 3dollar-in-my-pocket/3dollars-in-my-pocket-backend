package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.api.service.StoreSetupTest;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class VisitHistoryServiceTest extends StoreSetupTest {

    @Autowired
    private VisitHistoryService visitHistoryService;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        visitHistoryRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @DisplayName("가게 방문 인증 등록")
    @Nested
    class AddStoreVisitHistory {

        @Test
        void 유저가_가게_방문_인증을_등록하면_DB_에_가게_방문_기록이_저장된다() {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, VisitType.EXISTS);

            // when
            visitHistoryService.addVisitHistory(request, userId);

            // then
            List<VisitHistory> histories = visitHistoryRepository.findAll();
            assertAll(
                () -> assertThat(histories).hasSize(1),
                () -> assertVisitHistory(histories.get(0), storeId, userId, request.getType(), LocalDate.now())
            );
        }

        @Test
        void 가게_방문_인증시_존재하지_않은_가게인경우_NotFoundException_이_발생한다() {
            Long notFoundStoreId = 999L;
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(notFoundStoreId, VisitType.EXISTS);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 가게_방문_인증시_해당_유저가_오늘_방문한_가게인경우_ConflictException_이_발생한다() {
            // given
            LocalDate dateOfVisit = LocalDate.now(); // TODO 날짜와 분리시켜서 테스트할 수 있도록 개선해야함
            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, dateOfVisit));

            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, VisitType.NOT_EXISTS);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId)).isInstanceOf(ConflictException.class);
        }
    }

    private void assertVisitHistory(VisitHistory visitHistory, Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        assertThat(visitHistory.getStore().getId()).isEqualTo(storeId);
        assertThat(visitHistory.getUserId()).isEqualTo(userId);
        assertThat(visitHistory.getType()).isEqualTo(type);
        assertThat(visitHistory.getDateOfVisit()).isEqualTo(dateOfVisit);
    }

}
