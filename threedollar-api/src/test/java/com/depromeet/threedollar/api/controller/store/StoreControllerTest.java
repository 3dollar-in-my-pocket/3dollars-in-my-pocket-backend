package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.controller.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreDeletedEvent;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static com.depromeet.threedollar.api.assertutils.assertStoreUtils.assertStoreInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @MockBean
    private AddUserMedalEventListener addUserMedalEventListener;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        storeDeleteRequestRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
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

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(appearanceDays)
                .paymentMethods(paymentMethods)
                .menus(Set.of(MenuRequest.of("메뉴 이름", "가격", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.registerStore(request, token, 200);

            // then
            assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(MenuCategoryType.BUNGEOPPANG));
        }

        @Test
        void 가게_등록시_메달을_획득하는_작업이_수행된다() throws Exception {
            // given
            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("가게 이름")
                .storeType(StoreType.STORE)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("메뉴 이름", "한 개에 만원", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeMockApiCaller.registerStore(request, token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByAddStore(any(StoreCreatedEvent.class));
        }

    }

    @DisplayName("PUT /api/v2/store")
    @Nested
    class 가게_정보_수정 {

        @AutoSource
        @ParameterizedTest
        void 가게_수정_성공시_수정된_가게_정보를_반환한다(String storeName, StoreType storeType, Set<DayOfTheWeek> appearanceDays, Set<PaymentMethodType> paymentMethods) throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "storeName");
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

        @AutoSource
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

        @AutoSource
        @ParameterizedTest
        void 가게_삭제_요청시_실제로_삭제되면_True를_반환한다(DeleteReasonType deleteReasonType) throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store, 1000L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store, 1001L, DeleteReasonType.NOSTORE)
            ));

            DeleteStoreRequest request = DeleteStoreRequest.testInstance(deleteReasonType);

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getIsDeleted()).isTrue();
        }

        @AutoSource
        @ParameterizedTest
        void 가게_삭제_요청시_메달을_획득하는_작업이_수행된다(DeleteReasonType reasonType) throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName");
            storeRepository.save(store);

            // when
            storeMockApiCaller.deleteStore(store.getId(), DeleteStoreRequest.testInstance(reasonType), token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByDeleteStore(any(StoreDeletedEvent.class));
        }

    }

}
