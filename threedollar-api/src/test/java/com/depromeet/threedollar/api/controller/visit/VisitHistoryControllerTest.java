package com.depromeet.threedollar.api.controller.visit;

import com.depromeet.threedollar.api.controller.SetupStoreControllerTest;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoriesScrollResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertStoreUtils.assertStoreInfoResponse;
import static com.depromeet.threedollar.api.assertutils.assertVisitHistoryUtils.assertVisitHistoryWithStoreResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class VisitHistoryControllerTest extends SetupStoreControllerTest {

    private VisitHistoryApiCaller visitHistoryApiCaller;

    @BeforeEach
    void setUp() {
        visitHistoryApiCaller = new VisitHistoryApiCaller(mockMvc, objectMapper);
    }

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        visitHistoryRepository.deleteAllInBatch();
        super.cleanup();
    }

    @DisplayName("POST /api/v2/store/visit")
    @Nested
    class AddVisitHistory {

        @AutoSource
        @ParameterizedTest
        void 가게_방문_인증_등록시_성공시_200_OK(VisitType visitType) throws Exception {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(store.getId(), visitType);

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData())
            );
        }

    }

    @DisplayName("GET /api/v2/store/visits/me")
    @Nested
    class RetrieveMyVisitHistories {

        @Test
        void 내가_방문_인증한_가게들의_정보와_방문_기록을_조회한다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testInstance(2, null);

            // when
            ApiResponse<VisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().getContents()).hasSize(2),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory2, store),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(1), visitHistory1, store)
            );
        }

        @Test
        void SIZE만큼의_방문_기록을_조회힌다_더_존재하면_마지막_방문기록의_ID가_nextCursor_로_넘어간다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testInstance(1, null);

            // when
            ApiResponse<VisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(visitHistory2.getId()),
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory2, store)
            );
        }

        @Test
        void 커서로_넘어온_방문기록_이전의_방문_기록들을_SIZE_만큼_조회한다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testInstance(2, visitHistory2.getId());

            // when
            ApiResponse<VisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory1, store)
            );
        }

        @Test
        void 아무런_방문_기록을_남기지_않았을경우() throws Exception {
            // given
            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testInstance(2, null);

            // when
            ApiResponse<VisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().getContents()).hasSize(0)
            );
        }

        @Test
        void 방문한_가게가_삭제됬을경우_해당_정보를_반환하돼_가게는_삭제표시된다() throws Exception {
            // given
            Store deletedStore = StoreCreator.createDeletedWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(deletedStore);

            VisitHistory visitHistory = VisitHistoryCreator.create(deletedStore, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            visitHistoryRepository.save(visitHistory);

            RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testInstance(2, null);

            // when
            ApiResponse<VisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> assertStoreInfoResponse(response.getData().getContents().get(0).getStore(), deletedStore),
                () -> assertThat(response.getData().getContents().get(0).getStore().getIsDeleted()).isTrue()
            );
        }

    }

}
