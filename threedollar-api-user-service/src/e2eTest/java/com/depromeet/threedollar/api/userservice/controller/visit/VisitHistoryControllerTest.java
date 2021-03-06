package com.depromeet.threedollar.api.userservice.controller.visit;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.SetupStoreControllerTest;
import com.depromeet.threedollar.api.userservice.controller.store.support.StoreAssertions;
import com.depromeet.threedollar.api.userservice.controller.visit.support.VisitHistoryAssertions;
import com.depromeet.threedollar.api.userservice.listener.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.rds.event.userservice.visit.VisitHistoryAddedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Nested
    class AddVisitHistoryApiTest {

        @Test
        void ?????????_??????_?????????_????????????() throws Exception {
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
        void ?????????_??????_?????????_????????????_??????_??????_?????????_????????????() throws Exception {
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
        void ??????_??????_?????????_????????????_?????????_??????_?????????_????????????() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

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
        void SIZE?????????_??????_?????????_????????????_???_????????????_?????????_???????????????_ID???_nextCursor_???_????????????() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

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
        void ?????????_?????????_????????????_?????????_??????_????????????_SIZE_??????_????????????() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

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
        void ?????????_??????_?????????_?????????_???????????????() throws Exception {
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
        void ?????????_?????????_??????????????????_??????_?????????_????????????_?????????_??????????????????() throws Exception {
            // given
            Store deletedStore = StoreFixture.createDefaultWithMenu(user.getId(), "?????? ??????", StoreStatus.DELETED);
            storeRepository.save(deletedStore);

            VisitHistory visitHistory = VisitHistoryFixture.create(deletedStore, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
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
