package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.api.controller.medal.UserMedalEventListener;
import com.depromeet.threedollar.domain.event.visit.VisitHistoryAddedEvent;
import com.depromeet.threedollar.api.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertVisitHistoryUtils.assertVisitHistory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class VisitHistoryServiceTest extends SetupStoreServiceTest {

    @Autowired
    private VisitHistoryService visitHistoryService;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @MockBean
    private UserMedalEventListener userMedalEventListener;

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
        void 유저가_가게_방문_인증_정보를_추가한다(LocalDate dateOfVisit, VisitType visitType) {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, visitType);

            // when
            visitHistoryService.addVisitHistory(request, userId, dateOfVisit);

            // then
            List<VisitHistory> histories = visitHistoryRepository.findAll();
            assertAll(
                () -> assertThat(histories).hasSize(1),
                () -> assertVisitHistory(histories.get(0), storeId, userId, visitType, dateOfVisit)
            );
        }

        @Test
        void 존재하지_않는_가게에_방문_인증시_NotFound_에러가_발생한다() {
            // given
            Long notFoundStoreId = -1L;
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(notFoundStoreId, VisitType.EXISTS);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId, LocalDate.of(2021, 11, 18))).isInstanceOf(NotFoundException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_방문_인증시_오늘_이미_방문인증한_가게면_Conflict_에러가_발생한다(LocalDate dateOfVisit, VisitType visitType) {
            // given
            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, dateOfVisit));

            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, visitType);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId, dateOfVisit)).isInstanceOf(ConflictException.class);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_방문시_메달을_획득하는_작업이_수행된다(LocalDate dateOfVisit, VisitType visitType) {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, visitType);

            // when
            visitHistoryService.addVisitHistory(request, userId, dateOfVisit);

            // then
            verify(userMedalEventListener, times(1)).addObtainableMedalsByVisitStore(any(VisitHistoryAddedEvent.class));
        }

    }

}
