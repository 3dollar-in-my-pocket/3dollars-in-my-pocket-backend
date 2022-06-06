package com.depromeet.threedollar.api.user.controller.visit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.user.controller.SetupStoreControllerTest;
import com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions;
import com.depromeet.threedollar.api.user.controller.visit.support.VisitHistoryAssertions;
import com.depromeet.threedollar.api.user.listener.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.user.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.user.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.rds.event.userservice.visit.VisitHistoryAddedEvent;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryCreator;

class VisitHistoryControllerTest extends SetupStoreControllerTest {

    private VisitHistoryApiCaller visitHistoryApiCaller;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @MockBean
    private AddUserMedalEventListener addUserMedalEventListener;

    @BeforeEach
    void setUp() {
        visitHistoryApiCaller = new VisitHistoryApiCaller(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        visitHistoryRepository.deleteAllInBatch();
        super.cleanup();
    }

    @Nested
    class AddVisitHistoryApiTest {

        @Test
        void 가게에_방문_인증을_등록한다() throws Exception {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testBuilder()
                .storeId(store.getId())
                .type(VisitType.EXISTS)
                .build();

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 200);

            // then
            assertThat(response.getData()).isEqualTo(ApiResponse.OK.getData());
        }

        @Test
        void 가게에_방문_인증을_등록하면_메달_획득_작업이_실행된다() throws Exception {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testBuilder()
                .storeId(store.getId())
                .type(VisitType.NOT_EXISTS)
                .build();

            // when
            visitHistoryApiCaller.addVisitHistory(request, token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByVisitStore(any(VisitHistoryAddedEvent.class));
        }

    }

    @DisplayName("GET /api/v2/store/visits/me")
    @Nested
    class RetrieveMyVisitHistoriesApiTest {

        @Test
        void 내가_방문_인증한_가게들의_정보와_방문_기록을_조회한다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, user.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<VisitHistoriesCursorResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(2),
                () -> VisitHistoryAssertions.assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory2, store),
                () -> VisitHistoryAssertions.assertVisitHistoryWithStoreResponse(response.getData().getContents().get(1), visitHistory1, store)
            );
        }

        @Test
        void SIZE만큼의_방문_기록을_조회힌다_더_존재하면_마지막_방문기록의_ID가_nextCursor_로_넘어간다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, user.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
                .size(1)
                .cursor(null)
                .build();

            // when
            ApiResponse<VisitHistoriesCursorResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(visitHistory2.getId()),
                () -> assertThat(response.getData().isHasNext()).isTrue(),
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> VisitHistoryAssertions.assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory2, store)
            );
        }

        @Test
        void 커서로_넘어온_방문기록_이전의_방문_기록들을_SIZE_만큼_조회한다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, user.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
                .size(2)
                .cursor(visitHistory2.getId())
                .build();

            // when
            ApiResponse<VisitHistoriesCursorResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> VisitHistoryAssertions.assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory1, store)
            );
        }

        @Test
        void 아무런_방문_기록을_남기지_않았을경우() throws Exception {
            // given
            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<VisitHistoriesCursorResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(0)
            );
        }

        @Test
        void 방문한_가게가_삭제됬을경우_해당_정보를_반환하돼_가게는_삭제표시된다() throws Exception {
            // given
            Store deletedStore = StoreCreator.createDefaultWithMenu(user.getId(), "가게 이름", StoreStatus.DELETED);
            storeRepository.save(deletedStore);

            VisitHistory visitHistory = VisitHistoryCreator.create(deletedStore, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            visitHistoryRepository.save(visitHistory);

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<VisitHistoriesCursorResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> StoreAssertions.assertStoreInfoResponse(response.getData().getContents().get(0).getStore(), deletedStore),
                () -> assertThat(response.getData().getContents().get(0).getStore().getIsDeleted()).isTrue()
            );
        }

    }

}
