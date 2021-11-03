package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.api.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveVisitHistoryRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class VisitHistoryServiceTest extends SetupStoreServiceTest {

    @Autowired
    private VisitHistoryService visitHistoryService;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        visitHistoryRepository.deleteAllInBatch();
        super.cleanup();
    }

    @DisplayName("가게 방문 인증 등록")
    @Nested
    class AddStoreVisitHistory {

        @AutoSource
        @ParameterizedTest
        void 유저가_가게_방문_인증을_등록하면_DB_에_가게_방문_기록이_저장된다(VisitType visitType) {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, visitType);

            // when
            visitHistoryService.addVisitHistory(request, userId);

            // then
            List<VisitHistory> histories = visitHistoryRepository.findAll();
            assertAll(
                () -> assertThat(histories).hasSize(1),
                () -> assertVisitHistory(histories.get(0), storeId, userId, visitType, LocalDate.now())
            );
        }

        @AutoSource
        @ParameterizedTest
        void 가게_방문_인증시_존재하지_않은_가게인경우_NotFoundException_이_발생한다(Long notFoundStoreId) {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(notFoundStoreId, VisitType.EXISTS);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId)).isInstanceOf(NotFoundException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_방문_인증시_해당_유저가_오늘_방문한_가게인경우_ConflictException_이_발생한다(VisitType visitType) {
            // given
            LocalDate dateOfVisit = LocalDate.now(); // TODO 날짜와 분리시켜서 테스트할 수 있도록 개선해야함
            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, dateOfVisit));

            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, visitType);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId)).isInstanceOf(ConflictException.class);
        }
    }

    @DisplayName("가게 방문 목록 조회")
    @Nested
    class RetrieveVisitHistories {

        @AutoSource
        @ParameterizedTest
        void 존재하지_않는_가게에_대한_방문_인증_조회시_Not_Found_Exception(Long notFoundStoreId) {
            // given
            RetrieveVisitHistoryRequest request = RetrieveVisitHistoryRequest.testInstance(notFoundStoreId, LocalDate.of(2021, 10, 20), LocalDate.of(2021, 10, 23));

            // when & then
            assertThatThrownBy(() -> visitHistoryService.retrieveVisitHistories(request)).isInstanceOf(NotFoundException.class);
        }
    }

    private void assertVisitHistory(VisitHistory visitHistory, Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        assertThat(visitHistory.getStore().getId()).isEqualTo(storeId);
        assertThat(visitHistory.getUserId()).isEqualTo(userId);
        assertThat(visitHistory.getType()).isEqualTo(type);
        assertThat(visitHistory.getDateOfVisit()).isEqualTo(dateOfVisit);
    }

}
