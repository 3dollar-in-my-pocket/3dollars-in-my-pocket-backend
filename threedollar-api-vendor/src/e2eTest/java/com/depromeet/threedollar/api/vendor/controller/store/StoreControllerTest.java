package com.depromeet.threedollar.api.vendor.controller.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.vendor.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.vendor.controller.store.support.StoreAssertions;
import com.depromeet.threedollar.api.vendor.listener.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.vendor.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.vendor.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.vendor.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.vendor.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.vendor.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.vendor.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.Store;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreType;
import com.depromeet.threedollar.domain.rds.vendor.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.rds.vendor.event.store.StoreDeletedEvent;

class StoreControllerTest extends SetupUserControllerTest {

    private StoreMockApiCaller storeMockApiCaller;

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

    @BeforeEach
    void setUp() {
        storeMockApiCaller = new StoreMockApiCaller(mockMvc, objectMapper);
    }

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
    class AddStoreApiTest {

        @Test
        void 새로운_가게를_등록합니다() throws Exception {
            // given
            String storeName = "가게 이름";
            StoreType storeType = StoreType.STORE;
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.MONDAY);
            Set<PaymentMethodType> paymentMethods = Set.of(PaymentMethodType.CASH, PaymentMethodType.ACCOUNT_TRANSFER);
            double latitude = 34.0;
            double longitude = 130.0;

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(appearanceDays)
                .paymentMethods(paymentMethods)
                .menus(Set.of(MenuRequest.of("팥 붕어빵", "2개에 천원", UserMenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.registerStore(request, token, 200);

            // then
            StoreAssertions.assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(UserMenuCategoryType.BUNGEOPPANG));
        }

        @Test
        void 새로운_가게_등록시_획득할_수_있는_메달_체크_이벤트가_발생됩니다() throws Exception {
            // given
            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("가게 이름")
                .storeType(StoreType.STORE)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("슈크림 붕어빵", "5개에 2천원", UserMenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeMockApiCaller.registerStore(request, token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByAddStore(any(StoreCreatedEvent.class));
        }

    }

    @DisplayName("PUT /api/v2/store")
    @Nested
    class UpdateStoreInfoApiTest {

        @Test
        void 가게의_정보를_수정합니다() throws Exception {
            // given
            String storeName = "가게 이름";
            StoreType storeType = StoreType.STORE;
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.MONDAY);
            Set<PaymentMethodType> paymentMethods = Set.of(PaymentMethodType.CASH, PaymentMethodType.ACCOUNT_TRANSFER);

            Store store = StoreCreator.createWithDefaultMenu(user.getId(), "storeName");
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
                .menus(Set.of(MenuRequest.of("팥붕 슈붕", "5개에 2천원", UserMenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.updateStore(store.getId(), request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getStoreId()).isEqualTo(store.getId()),
                () -> StoreAssertions.assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(UserMenuCategoryType.BUNGEOPPANG))
            );
        }

    }

    @DisplayName("DELETE /api/v2/store")
    @Nested
    class DeleteStoreApiTest {

        @Test
        void 가게_삭제_요청시_실제로_삭제되지_않으면_False를_반환한다() throws Exception {
            // given
            Store store = StoreCreator.create(user.getId(), "가삼 붕어빵");
            storeRepository.save(store);

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(DeleteReasonType.OVERLAPSTORE)
                .build();

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getIsDeleted()).isFalse();
        }

        @Test
        void 가게_삭제_요청시_실제로_삭제되면_True를_반환한다() throws Exception {
            // given
            Store store = StoreCreator.create(user.getId(), "가삼 붕어빵");
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store, 1000L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store, 1001L, DeleteReasonType.NOSTORE)
            ));

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(DeleteReasonType.WRONG_CONTENT)
                .build();

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getIsDeleted()).isTrue();
        }

        @Test
        void 가게_삭제_요청시_획득할_수_있는_메달_체크_이벤트가_발생됩니다() throws Exception {
            // given
            Store store = StoreCreator.create(user.getId(), "가슴속 3천원 붕어빵");
            storeRepository.save(store);

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(DeleteReasonType.OVERLAPSTORE)
                .build();

            // when
            storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByDeleteStore(any(StoreDeletedEvent.class));
        }

    }

}
