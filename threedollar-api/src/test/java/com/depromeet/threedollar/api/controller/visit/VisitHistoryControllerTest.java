package com.depromeet.threedollar.api.controller.visit;

import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class VisitHistoryControllerTest extends AbstractControllerTest {

    private VisitHistoryApiCaller visitHistoryApiCaller;

    private Store store;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        visitHistoryApiCaller = new VisitHistoryApiCaller(mockMvc, objectMapper);
        store = StoreCreator.create(testUser.getId(), "디프만 붕어빵");
        store.addMenus(Collections.singletonList(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
        storeRepository.save(store);
    }

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        visitHistoryRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @DisplayName("가게 방문 인증 등록")
    @Nested
    class AddVisitHistory {

        @Test
        void 정상적으로_가게_방문인증이_등록된다() throws Exception {
            // given
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(store.getId(), VisitType.EXISTS);

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData())
            );
        }

        @Test
        void 이미_오늘_해당_가게에_방문_인증을_한경우_409에러_발생() throws Exception {
            // given
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.now()));
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(store.getId(), VisitType.EXISTS);

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 409);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getCode()),
                () -> assertThat(response.getMessage()).isEqualTo(ErrorCode.CONFLICT_EXCEPTION.getMessage())
            );
        }

        @Test
        void 가게_방문_인증시_존재하지_않는_가게라면_404_에러가_발생한다() throws Exception {
            // given
            Long notFoundStoreId = 999L;
            AddVisitHistoryRequest request = AddVisitHistoryRequest.testInstance(notFoundStoreId, VisitType.EXISTS);

            // when
            ApiResponse<String> response = visitHistoryApiCaller.addVisitHistory(request, token, 404);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEqualTo(ErrorCode.NOT_FOUND_STORE_EXCEPTION.getCode()),
                () -> assertThat(response.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_STORE_EXCEPTION.getMessage())
            );
        }

    }

}
