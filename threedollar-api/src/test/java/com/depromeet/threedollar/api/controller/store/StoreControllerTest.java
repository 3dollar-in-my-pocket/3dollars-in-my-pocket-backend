package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_STORE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class StoreControllerTest extends SetupUserControllerTest {

    private StoreMockApiCaller storeMockApiCaller;

    @BeforeEach
    void setUp() {
        storeMockApiCaller = new StoreMockApiCaller(mockMvc, objectMapper);
    }

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AppearanceDayRepository appearanceDayRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreDeleteRequestRepository storeDeleteRequestRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        storeRepository.deleteAll();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        storeDeleteRequestRepository.deleteAll();
    }

    @DisplayName("POST /api/v2/store")
    @Nested
    class 가게_정보_등록 {

        @AutoSource
        @ParameterizedTest
        void 가게_등록_성공시_가게_정보를_반환한다(String storeName, StoreType storeType, Set<DayOfTheWeek> appearanceDays, Set<PaymentMethodType> paymentMethods) throws Exception {
            // given
            double latitude = 34.0;
            double longitude = 130.0;

            AddStoreRequest request = AddStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(appearanceDays)
                .paymentMethods(paymentMethods)
                .menus(Set.of(MenuRequest.of("메뉴 이름", "가격", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.addStore(request, token, 200);

            // then
            assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(MenuCategoryType.BUNGEOPPANG));
        }

    }

    @DisplayName("PUT /api/v2/store")
    @Nested
    class 가게_정보_수정 {

        @AutoSource
        @ParameterizedTest
        void 가게_수정_성공시_수정된_가게_정보를_반환한다(String storeName, StoreType storeType, Set<DayOfTheWeek> appearanceDays, Set<PaymentMethodType> paymentMethods) throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName");
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            double latitude = 34.0;
            double longitude = 130.0;

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(appearanceDays)
                .paymentMethods(paymentMethods)
                .menus(Set.of(MenuRequest.of("메뉴 이름", "가격", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.updateStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getStoreId()).isEqualTo(store.getId());
            assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(MenuCategoryType.BUNGEOPPANG));
        }

    }

    @DisplayName("DELETE /api/v2/store")
    @Nested
    class 가게_정보_삭제 {

        @EnumSource
        @ParameterizedTest
        void 가게_삭제_요청시_실제로_삭제되지_않으면_False를_반환한다(DeleteReasonType deleteReasonType) throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName");
            storeRepository.save(store);

            DeleteStoreRequest request = DeleteStoreRequest.testInstance(deleteReasonType);

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getIsDeleted()).isFalse();
        }

        @EnumSource
        @ParameterizedTest
        void 가게_삭제_요청시_실제로_삭제되면_True를_반환한다(DeleteReasonType deleteReasonType) throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store.getId(), 10L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store.getId(), 11L, DeleteReasonType.NOSTORE)
            ));

            DeleteStoreRequest request = DeleteStoreRequest.testInstance(deleteReasonType);

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getIsDeleted()).isTrue();
        }

        @EnumSource
        @ParameterizedTest
        void 가게_삭제_요청시_존재하지_않는_가게인경우_404_NOT_FOUND(DeleteReasonType deleteReasonType) throws Exception {
            // given
            Long notFoundStoreId = -1L;
            DeleteStoreRequest request = DeleteStoreRequest.testInstance(deleteReasonType);

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(notFoundStoreId, request, token, 404);

            // then
            assertThat(response.getResultCode()).isEqualTo(NOT_FOUND_STORE_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(NOT_FOUND_STORE_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

    }

    private void assertStoreInfoResponse(StoreInfoResponse response, double latitude, double longitude, String storeName, List<MenuCategoryType> categories) {
        assertThat(response.getLatitude()).isEqualTo(latitude);
        assertThat(response.getLongitude()).isEqualTo(longitude);
        assertThat(response.getStoreName()).isEqualTo(storeName);
        assertThat(response.getCategories()).isEqualTo(categories);
    }

}
