package com.depromeet.threedollar.api.user.service.visit;

import com.depromeet.threedollar.api.user.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.user.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.api.user.testhelper.assertions.VisitHistoryAssertionHelper.assertVisitHistory;
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

        @Test
        void 유저가_가게_방문_인증_정보를_추가한다() {
            // given
            LocalDate dateOfVisit = LocalDate.of(2021, 12, 1);
            VisitType visitType = VisitType.EXISTS;

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

        @Test
        void 가게_방문_인증시_오늘_이미_방문인증한_가게면_Conflict_에러가_발생한다() {
            // given
            LocalDate dateOfVisit = LocalDate.of(2021, 12, 1);
            VisitType visitType = VisitType.EXISTS;

            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, dateOfVisit));

            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(storeId, visitType);

            // when & then
            assertThatThrownBy(() -> visitHistoryService.addVisitHistory(request, userId, dateOfVisit)).isInstanceOf(ConflictException.class);
        }

    }

}