package com.depromeet.threedollar.api.user.controller.store;

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
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions;
import com.depromeet.threedollar.api.user.listener.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.user.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.domain.rds.user.domain.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreType;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreWithMenuCreator;
import com.depromeet.threedollar.domain.rds.user.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.rds.user.event.store.StoreDeletedEvent;

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
        void 가게_등록_성공시_가게_정보를_반환한다() throws Exception {
            // given
            String storeName = "가슴속 삼천원 붕어빵 가게";
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
                .menus(Set.of(MenuRequest.of("팥 붕어빵", "2개에 천원", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.registerStore(request, token, 200);

            // then
            StoreAssertions.assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(MenuCategoryType.BUNGEOPPANG));
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
                .menus(Set.of(MenuRequest.of("슈크림 붕어빵", "5개에 2천원", MenuCategoryType.BUNGEOPPANG)))
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
        void 가게_수정_성공시_수정된_가게_정보를_반환한다() throws Exception {
            // given
            String storeName = "새로운 가게의 이름";
            StoreType storeType = StoreType.STORE;
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.MONDAY);
            Set<PaymentMethodType> paymentMethods = Set.of(PaymentMethodType.CASH, PaymentMethodType.ACCOUNT_TRANSFER);

            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("기존의 가게 이름")
                .build();
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
                .menus(Set.of(MenuRequest.of("팥&슈크림 붕어빵", "5개에 2천원", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            ApiResponse<StoreInfoResponse> response = storeMockApiCaller.updateStore(store.getId(), request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getStoreId()).isEqualTo(store.getId()),
                () -> StoreAssertions.assertStoreInfoResponse(response.getData(), latitude, longitude, storeName, List.of(MenuCategoryType.BUNGEOPPANG))
            );
        }

    }

    @DisplayName("DELETE /api/v2/store")
    @Nested
    class DeleteStoreApiTest {

        @Test
        void 가게_삭제_요청시_실제로_삭제되지_않으면_False를_반환한다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가슴속 삼천원 가게")
                .build();
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
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("등록된 가게의 이름")
                .build();
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                    StoreDeleteRequestCreator.builder()
                        .store(store)
                        .userId(1000L)
                        .reasonType(DeleteReasonType.NOSTORE)
                        .build(),
                    StoreDeleteRequestCreator.builder()
                        .store(store)
                        .userId(1001L)
                        .reasonType(DeleteReasonType.NOSTORE)
                        .build()
                )
            );

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(DeleteReasonType.WRONG_CONTENT)
                .build();

            // when
            ApiResponse<StoreDeleteResponse> response = storeMockApiCaller.deleteStore(store.getId(), request, token, 200);

            // then
            assertThat(response.getData().getIsDeleted()).isTrue();
        }

        @Test
        void 가게_삭제_요청시_메달을_획득하는_작업이_수행된다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("토순이의 붕어빵")
                .build();
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
