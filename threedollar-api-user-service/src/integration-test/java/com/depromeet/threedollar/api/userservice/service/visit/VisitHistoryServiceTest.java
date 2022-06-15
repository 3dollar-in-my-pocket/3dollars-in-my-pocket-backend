package com.depromeet.threedollar.api.userservice.service.visit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.userservice.SetupStoreIntegrationTest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.userservice.service.visit.support.VisitHistoryAssertions;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;

class VisitHistoryServiceTest extends SetupStoreIntegrationTest {

    @Autowired
    private VisitHistoryService visitHistoryService;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Nested
    class AddStoreVisitHistoryTest {

        @Test
        void 새로운_가게_방문_인증_정보를_추가한다() {
            // given
            LocalDate dateOfVisit = LocalDate.of(2021, 12, 1);
            VisitType visitType = VisitType.EXISTS;

            AddVisitHistoryRequest request = AddVisitHistoryRequest.testBuilder()
                .storeId(storeId)
                .type(visitType)
                .build();

            // when
            visitHistoryService.addVisitHistory(request, userId, dateOfVisit);

            // then
            List<VisitHistory> histories = visitHistoryRepository.findAll();
            assertAll(
                () -> assertThat(histories).hasSize(1),
                () -> VisitHistoryAssertions.assertVisitHistory(histories.get(0), storeId, userId, visitType, dateOfVisit)
            );
        }

        @Test
        void 존재하지_않는_가게에_방문_인증시_NotFound_에러가_발생한다() {
            // given
            Long notFoundStoreId = -1L;
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testBuilder()
                .storeId(notFoundStoreId)
                .type(VisitType.EXISTS)
                .build();

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId, LocalDate.of(2021, 11, 18))).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 가게_방문_인증시_오늘_이미_방문인증한_가게면_Conflict_에러가_발생한다() {
            // given
            LocalDate dateOfVisit = LocalDate.of(2021, 12, 1);
            VisitType visitType = VisitType.EXISTS;

            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, dateOfVisit));

            AddVisitHistoryRequest request = AddVisitHistoryRequest.testBuilder()
                .storeId(storeId)
                .type(visitType)
                .build();

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId, dateOfVisit)).isInstanceOf(ConflictException.class);
        }

    }

}
