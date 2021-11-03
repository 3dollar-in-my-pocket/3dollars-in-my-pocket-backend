package com.depromeet.threedollar.api.controller.visit;

import com.depromeet.threedollar.api.controller.SetupStoreControllerTest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveMyVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.MyVisitHistoriesScrollResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithStoreResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class VisitHistoryControllerTest extends SetupStoreControllerTest {

    private VisitHistoryApiCaller visitHistoryApiCaller;

    @BeforeEach
    void setUp() {
        visitHistoryApiCaller = new VisitHistoryApiCaller(mockMvc, objectMapper);
    }

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        visitHistoryRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        super.cleanup();
    }

    @DisplayName("POST /api/v2/store/visit")
    @Nested
    class AddVisitHistory {

        @EnumSource
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

        @EnumSource
        @ParameterizedTest
        void 가게_방문_인증_등록시_이미_오늘_해당_가게에_방문_인증을_한경우_409에러_발생(VisitType visitType) throws Exception {
            // given
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.now()));
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(store.getId(), visitType);

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 409);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getCode()),
                () -> assertThat(response.getMessage()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getMessage())
            );
        }

        @EnumSource
        @ParameterizedTest
        void 가게_방문_인증시_존재하지_않는_가게라면_404_에러가_발생한다(VisitType visitType) throws Exception {
            // given
            Long notFoundStoreId = -1L;
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(notFoundStoreId, visitType);

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 404);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEqualTo(ErrorCode.NOT_FOUND_STORE_EXCEPTION.getCode()),
                () -> assertThat(response.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_STORE_EXCEPTION.getMessage())
            );
        }

    }

    @DisplayName("GET /api/v2/store/visits")
    @Nested
    class RetrieveVisitHistories {

        @Test
        void 가게에_등록된_방문_기록들을_일자별로_조회한다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveVisitHistoryRequest request = RetrieveVisitHistoryRequest.testInstance(store.getId(), LocalDate.of(2021, 10, 21), LocalDate.of(2021, 10, 22));

            // when
            ApiResponse<List<VisitHistoryWithUserResponse>> response = visitHistoryApiCaller.retrieveVisitHistories(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertVisitHistoryWithUserResponse(response.getData().get(0), visitHistory1, store, testUser),
                () -> assertVisitHistoryWithUserResponse(response.getData().get(1), visitHistory2, store, testUser)
            );
        }

        @Test
        void 가게에_등록된_방문_기록들을_같은날_여러_기록이_있는경우() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 21));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveVisitHistoryRequest request = RetrieveVisitHistoryRequest.testInstance(store.getId(), LocalDate.of(2021, 10, 21), LocalDate.of(2021, 10, 22));

            // when
            ApiResponse<List<VisitHistoryWithUserResponse>> response = visitHistoryApiCaller.retrieveVisitHistories(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertVisitHistoryWithUserResponse(response.getData().get(0), visitHistory1, store, testUser),
                () -> assertVisitHistoryWithUserResponse(response.getData().get(1), visitHistory2, store, testUser)
            );
        }

        @Test
        void 가게_방문_기록_조회시_존재하지_않는_가게면_404_NotFound() throws Exception {
            // given
            Long notFoundStoreId = -1L;
            RetrieveVisitHistoryRequest request = RetrieveVisitHistoryRequest.testInstance(notFoundStoreId, LocalDate.of(2021, 10, 21), LocalDate.of(2021, 10, 22));

            // when
            ApiResponse<List<VisitHistoryWithUserResponse>> response = visitHistoryApiCaller.retrieveVisitHistories(request, 404);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEqualTo(ErrorCode.NOT_FOUND_STORE_EXCEPTION.getCode()),
                () -> assertThat(response.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_STORE_EXCEPTION.getMessage())
            );
        }

        private void assertVisitHistoryWithUserResponse(VisitHistoryWithUserResponse response, VisitHistory visitHistory, Store store, User user) {
            assertAll(
                () -> assertThat(response.getVisitHistoryId()).isEqualTo(visitHistory.getId()),
                () -> assertThat(response.getType()).isEqualTo(visitHistory.getType()),
                () -> assertThat(response.getStoreId()).isEqualTo(store.getId()),
                () -> assertUserInfoResponse(response.getUser(), user)
            );
        }

        private void assertUserInfoResponse(UserInfoResponse response, User user) {
            assertAll(
                () -> assertThat(response.getName()).isEqualTo(user.getName()),
                () -> assertThat(response.getUserId()).isEqualTo(user.getId()),
                () -> assertThat(response.getSocialType()).isEqualTo(user.getSocialType())
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

            RetrieveMyVisitHistoryRequest request = RetrieveMyVisitHistoryRequest.testInstance(2, null);

            // when
            ApiResponse<MyVisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertStoreInfoResponse(response.getData().getContents().get(0).getStore(), store),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory2),

                () -> assertStoreInfoResponse(response.getData().getContents().get(1).getStore(), store),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(1), visitHistory1)
            );
        }

        @Test
        void SIZE만큼의_방문_기록을_조회힌다_더_존재하면_마지막_방문기록의_ID가_nextCursor_로_넘어간다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoryRequest request = RetrieveMyVisitHistoryRequest.testInstance(1, null);

            // when
            ApiResponse<MyVisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(visitHistory2.getId()),
                () -> assertThat(response.getData().getContents()).hasSize(1),

                () -> assertStoreInfoResponse(response.getData().getContents().get(0).getStore(), store),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory2)
            );
        }

        @Test
        void 커서로_넘어온_방문기록_이전의_방문_기록들을_SIZE_만큼_조회한다() throws Exception {
            // given
            VisitHistory visitHistory1 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 21));
            VisitHistory visitHistory2 = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 22));

            visitHistoryRepository.saveAll(List.of(visitHistory1, visitHistory2));

            RetrieveMyVisitHistoryRequest request = RetrieveMyVisitHistoryRequest.testInstance(2, visitHistory2.getId());

            // when
            ApiResponse<MyVisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().getContents()).hasSize(1),

                () -> assertStoreInfoResponse(response.getData().getContents().get(0).getStore(), store),
                () -> assertVisitHistoryWithStoreResponse(response.getData().getContents().get(0), visitHistory1)
            );
        }

        @Test
        void 아무런_방문_기록을_남기지_않았을경우() throws Exception {
            // given
            RetrieveMyVisitHistoryRequest request = RetrieveMyVisitHistoryRequest.testInstance(2, null);

            // when
            ApiResponse<MyVisitHistoriesScrollResponse> response = visitHistoryApiCaller.retrieveMyVisitHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().getContents()).hasSize(0)
            );
        }

        private void assertVisitHistoryWithStoreResponse(VisitHistoryWithStoreResponse response, VisitHistory visitHistory) {
            assertAll(
                () -> assertThat(response.getVisitHistoryId()).isEqualTo(visitHistory.getId()),
                () -> assertThat(response.getDateOfVisit()).isEqualTo(visitHistory.getDateOfVisit()),
                () -> assertThat(response.getType()).isEqualTo(visitHistory.getType())
            );
        }

        private void assertStoreInfoResponse(StoreInfoResponse response, Store store) {
            assertAll(
                () -> assertThat(response.getStoreId()).isEqualTo(store.getId()),
                () -> assertThat(response.getStoreName()).isEqualTo(store.getName()),
                () -> assertThat(response.getCategories()).isEqualTo(store.getMenuCategories())
            );
        }

    }

}
