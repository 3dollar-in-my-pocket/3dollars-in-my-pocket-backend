package com.depromeet.threedollar.api.userservice.controller.store;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.SetupUserControllerTest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.CheckExistsStoresNearbyRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveAroundStoresRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.CheckExistStoresNearbyResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoresCursorResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.type.UserStoreOrderType;
import com.depromeet.threedollar.common.model.LocationValue;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Menu;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.redis.domain.userservice.store.AroundUserStoresCacheRepository;
import com.depromeet.threedollar.domain.redis.domain.userservice.store.UserStoreCacheModel;
import com.depromeet.threedollar.domain.redis.domain.userservice.store.UserStoreCacheModelFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.depromeet.threedollar.api.userservice.controller.review.support.ReviewAssertions.assertReviewWithWriterResponse;
import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreAssertions.assertMenuResponse;
import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreAssertions.assertStoreDetailInfoResponse;
import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreAssertions.assertStoreWithVisitsAndDistanceResponse;
import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreAssertions.assertStoreWithVisitsResponse;
import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreImageAssertions.assertStoreImageResponse;
import static com.depromeet.threedollar.api.userservice.controller.user.support.UserAssertions.assertUserInfoResponse;
import static com.depromeet.threedollar.api.userservice.controller.visit.support.VisitHistoryAssertions.assertVisitHistoryInfoResponse;
import static com.depromeet.threedollar.api.userservice.controller.visit.support.VisitHistoryAssertions.assertVisitHistoryWithUserResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StoreRetrieveControllerTest extends SetupUserControllerTest {

    private StoreRetrieveMockApiCaller storeRetrieveMockApiCaller;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @MockBean
    private AroundUserStoresCacheRepository aroundUserStoresCacheRepository;

    @BeforeEach
    void setUp() {
        storeRetrieveMockApiCaller = new StoreRetrieveMockApiCaller(mockMvc, objectMapper);
    }

    @DisplayName("GET /api/v2/stores/near")
    @Nested
    class RetrieveAroundStoresApiTest {

        @BeforeEach
        void disableCached() {
            when(aroundUserStoresCacheRepository.getCache(anyDouble(), anyDouble(), anyDouble())).thenReturn(null);
            doNothing().when(aroundUserStoresCacheRepository).setCache(anyDouble(), anyDouble(), anyDouble(), anyList());
        }

        @Test
        void ??????_??????_?????????_?????????_??????_?????????_???????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "????????? ?????? 1", 34, 126);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "????????? ?????? 2", 34, 126);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertStoreWithVisitsAndDistanceResponse(response.getData().get(0), store1.getId(), store1.getLatitude(), store1.getLongitude(), store1.getName(), store1.getRating()),
                () -> assertStoreWithVisitsAndDistanceResponse(response.getData().get(1), store2.getId(), store2.getLatitude(), store2.getLongitude(), store2.getName(), store2.getRating())
            );
        }

        @Test
        void ??????_??????_?????????_?????????_??????_?????????_?????????_??????_????????????_?????????_????????????() throws Exception {
            // given
            Store store = StoreFixture.create(user.getId(), "????????? ??????", 34.0, 126.0);
            store.addMenus(List.of(
                MenuFixture.create(store, "??????", "2??? ??????", UserMenuCategoryType.SUNDAE),
                MenuFixture.create(store, "??????", "2??? ??????", UserMenuCategoryType.DALGONA)
            ));
            storeRepository.save(store);

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),

                () -> assertThat(response.getData().get(0).getCategories()).hasSize(2),
                () -> assertThat(response.getData().get(0).getCategories()).containsExactlyInAnyOrder(UserMenuCategoryType.SUNDAE, UserMenuCategoryType.DALGONA)
            );
        }

        @Test
        void ??????_??????_?????????_????????????_?????????_?????????_????????????_?????????() throws Exception {
            // given
            Store deletedStoreByUser = StoreFixture.create(user.getId(), "???????????? ??? ?????????", 34.0, 126.0, 1.0, StoreStatus.DELETED);
            Store deletedStoreByAdmin = StoreFixture.create(user.getId(), "???????????? ??? ?????????", 34.0, 126.0, 1.0, StoreStatus.FILTERED);
            storeRepository.saveAll(List.of(deletedStoreByUser, deletedStoreByAdmin));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertThat(response.getData()).isEmpty();
        }

        @Test
        void ??????_??????_??????_?????????_????????????_??????_??????_????????????_????????????() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "?????? ??????", 34.0, 126.0);
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.saveAll(List.of(
                VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, today.minusMonths(1)),
                VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, today.minusWeeks(1)),
                VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, today)
            ));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().get(0).getVisitHistory(), 2, 1, true);
        }

        @Test
        void ??????_????????????_????????????_?????????_??????_???????????????_??????_????????????_????????????_?????????() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "?????? ??????", 34.0, 126.0);
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.save(VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, today.minusMonths(1).minusDays(1)));
            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertVisitHistoryInfoResponse(response.getData().get(0).getVisitHistory(), 0, 0, false)
            );
        }

        @Test
        void ???_?????????_??????_?????????_????????????_??????_??????_???????????????_???????????????_???????????????() throws Exception {
            // given
            Store store1 = StoreFixture.create(user.getId(), "??????1", 34, 126);
            store1.addMenus(List.of(MenuFixture.create(store1, "??????2", "??????2", UserMenuCategoryType.DALGONA)));

            Store store2 = StoreFixture.create(user.getId(), "??????1", 34, 126);
            store2.addMenus(List.of(MenuFixture.create(store2, "??????2", "??????2", UserMenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(List.of(store1, store2));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(1000)
                .category(UserMenuCategoryType.DALGONA)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId())
            );
        }

        @Test
        void ???_?????????_??????_?????????_????????????_???????????????_DISTANCE_ASC???_?????????_?????????_?????????_?????????_????????????_???????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1", 34.00015, 126);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2", 34.0001, 126);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(2000)
                .orderType(UserStoreOrderType.DISTANCE_ASC)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store2.getId()),
                () -> assertThat(response.getData().get(1).getStoreId()).isEqualTo(store1.getId())
            );
        }

        @Test
        void ???_?????????_??????_?????????_????????????_???????????????_REVIEW_DESC???_?????????_??????_??????_????????????_???????????????_????????????_???????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1", 34.00015, 126, 5);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2", 34.0001, 126, 1);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(2000)
                .orderType(UserStoreOrderType.REVIEW_DESC)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId()),
                () -> assertThat(response.getData().get(1).getStoreId()).isEqualTo(store2.getId())
            );
        }

        @Test
        void ???_?????????_?????????_????????????_?????????_?????????_????????????_??????_???????????????_??????_SIZE_??????_????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1", 34.00015, 126, 5);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2", 34.0001, 126, 1);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(2000)
                .orderType(UserStoreOrderType.REVIEW_DESC)
                .size(1)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId())
            );
        }

        @Test
        void ???_?????????_?????????_????????????_??????_???????????????_???????????????_??????_???????????????_??????_SIZE_??????_????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1", 34.00015, 126, 5);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2", 34.0001, 126, 1);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(2000)
                .orderType(UserStoreOrderType.REVIEW_DESC)
                .size(1)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(34.0, 126.0), LocationValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId())
            );
        }

    }

    @DisplayName("GET /api/v2/stores/near cache check")
    @Nested
    class RetrieveAroundStoreApiCacheCheck {

        @Test
        void ?????????_??????_??????_????????????_????????????_DB??????_?????????_???????????????_?????????_????????????() throws Exception {
            // given
            double latitude = 34.0;
            double longitude = 126.0;
            double distance = 1000;

            when(aroundUserStoresCacheRepository.getCache(anyDouble(), anyDouble(), anyDouble())).thenReturn(null);

            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "????????? ?????? 1", latitude, longitude);
            storeRepository.save(store);

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(distance)
                .build();

            // when
            storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(latitude, longitude), LocationValue.of(latitude, longitude), 200);

            // then
            verify(aroundUserStoresCacheRepository, times(1)).setCache(anyDouble(), anyDouble(), anyDouble(), anyList());
        }

        @Test
        void ?????????_??????_??????_????????????_?????????_DB_????????????_????????????_????????????_????????????() throws Exception {
            // given
            double latitude = 34.0;
            double longitude = 126.0;
            double distance = 1000;

            UserStoreCacheModel cachedStore = UserStoreCacheModelFixture.create(
                100L,
                latitude,
                longitude
            );

            when(aroundUserStoresCacheRepository.getCache(anyDouble(), anyDouble(), anyDouble())).thenReturn(List.of(cachedStore));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(distance)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(latitude, longitude), LocationValue.of(latitude, longitude), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertStoreWithVisitsAndDistanceResponse(response.getData().get(0), cachedStore.getStoreId(), cachedStore.getLatitude(), cachedStore.getLongitude(), cachedStore.getStoreName(), cachedStore.getRating())
            );
        }

        @Test
        void ?????????_??????_????????????_???????????????_???????????????_????????????() throws Exception {
            // given
            double latitude = 35.0;
            double longitude = 127.0;
            double distance = 1000;

            UserStoreCacheModel noMatchedStore = UserStoreCacheModelFixture.create(
                100L,
                latitude,
                longitude,
                List.of(UserMenuCategoryType.SUNDAE, UserMenuCategoryType.WAFFLE)
            );
            UserStoreCacheModel matchedStore = UserStoreCacheModelFixture.create(
                100L,
                latitude,
                longitude,
                List.of(UserMenuCategoryType.SUNDAE, UserMenuCategoryType.BUNGEOPPANG)
            );

            when(aroundUserStoresCacheRepository.getCache(anyDouble(), anyDouble(), anyDouble())).thenReturn(List.of(noMatchedStore, matchedStore));

            RetrieveAroundStoresRequest request = RetrieveAroundStoresRequest.testBuilder()
                .distance(distance)
                .category(UserMenuCategoryType.BUNGEOPPANG)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.retrieveAroundStores(request, LocationValue.of(latitude, longitude), LocationValue.of(latitude, longitude), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertStoreWithVisitsAndDistanceResponse(response.getData().get(0), matchedStore.getStoreId(), matchedStore.getLatitude(), matchedStore.getLongitude(), matchedStore.getStoreName(), matchedStore.getRating())
            );
        }

    }

    @DisplayName("GET /api/v2/store")
    @Nested
    class RetrieveStoreDetailApiTest {

        @Test
        void ??????_??????_?????????_????????????() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "?????? ??????");
            DayOfTheWeek day = DayOfTheWeek.FRIDAY;
            store.addAppearanceDays(Set.of(day));

            PaymentMethodType paymentMethodType = PaymentMethodType.CASH;
            store.addPaymentMethods(Set.of(paymentMethodType));

            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertStoreDetailInfoResponse(data, store, user);
        }

        @Test
        void ??????_??????_??????_?????????_??????_?????????_??????_????????????() throws Exception {
            // given
            Store store = StoreFixture.create();
            Menu menu1 = MenuFixture.create(store, "??????1", "??????1", UserMenuCategoryType.BUNGEOPPANG);
            Menu menu2 = MenuFixture.create(store, "??????2", "??????2", UserMenuCategoryType.GUKWAPPANG);
            store.addMenus(List.of(menu1, menu2));
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getCategories()).containsExactlyInAnyOrderElementsOf(List.of(UserMenuCategoryType.BUNGEOPPANG, UserMenuCategoryType.GUKWAPPANG)),
                () -> assertThat(response.getData().getMenus()).hasSize(2),
                () -> assertMenuResponse(response.getData().getMenus().get(0), menu1),
                () -> assertMenuResponse(response.getData().getMenus().get(1), menu2)
            );
        }

        @Test
        void ??????_??????_??????_?????????_?????????_?????????_??????_????????????() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertUserInfoResponse(response.getData().getUser(), user);
        }

        @Test
        void ??????_??????_??????_?????????_?????????_?????????_??????_????????????() throws Exception {
            // given
            Set<DayOfTheWeek> dayOfTheWeeks = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY, DayOfTheWeek.THURSDAY);
            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            store.addAppearanceDays(dayOfTheWeeks);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getAppearanceDays()).hasSize(dayOfTheWeeks.size()),
                () -> assertThat(response.getData().getAppearanceDays()).containsExactlyInAnyOrderElementsOf(dayOfTheWeeks)
            );
        }

        @Test
        void ??????_??????_??????_?????????_??????_??????_?????????_??????_????????????() throws Exception {
            // given
            Set<PaymentMethodType> paymentMethodTypes = Set.of(PaymentMethodType.CASH, PaymentMethodType.CARD);
            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            store.addPaymentMethods(paymentMethodTypes);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getPaymentMethods()).hasSize(paymentMethodTypes.size()),
                () -> assertThat(response.getData().getPaymentMethods()).containsExactlyInAnyOrderElementsOf(paymentMethodTypes)
            );
        }

        @Test
        void ??????_??????_??????_?????????_???????????????_??????????????????_?????????_????????????_????????????() throws Exception {
            // given
            long notFoundUserId = -1L;

            Store store = StoreFixture.createWithDefaultMenu(notFoundUserId);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertUserInfoResponse(data.getUser(), null, "????????? ?????????", null);
        }

        @Test
        void ??????_??????_??????_?????????_?????????_?????????_?????????_?????????_????????????() throws Exception {
            // given
            String imageUrl = "https://image.url";

            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            StoreImage storeImage = StoreImageFixture.create(store.getId(), user.getId(), imageUrl);
            storeImageRepository.save(storeImage);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getImages()).hasSize(1),
                () -> assertStoreImageResponse(response.getData().getImages().get(0), storeImage.getId(), imageUrl)
            );
        }

        @Test
        void ??????_??????_??????_?????????_?????????_????????????_????????????_?????????() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            StoreImage storeImage = StoreImageFixture.createDeleted(store.getId());
            storeImageRepository.save(storeImage);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertThat(response.getData().getImages()).isEmpty();
        }

        @Test
        void ??????_??????_?????????_?????????_??????_?????????_?????????_???????????????_????????????_????????????() throws Exception {
            // given
            long notFoundUserId = -1L;

            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            Review review1 = ReviewFixture.create(store.getId(), user.getId(), "?????? 1", 5);
            Review review2 = ReviewFixture.create(store.getId(), notFoundUserId, "?????? 2", 3);

            reviewRepository.saveAll(List.of(review1, review2));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertAll(
                () -> assertThat(data.getReviews()).hasSize(2),
                () -> assertReviewWithWriterResponse(data.getReviews().get(0), review2),
                () -> assertUserInfoResponse(data.getReviews().get(0).getUser(), null, "????????? ?????????", null),

                () -> assertReviewWithWriterResponse(data.getReviews().get(1), review1),
                () -> assertUserInfoResponse(data.getReviews().get(1).getUser(), user)
            );
        }

        @Test
        void ??????_??????_?????????_????????????_??????_??????_????????????_????????????() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            visitHistoryRepository.save(VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, today.minusMonths(1)));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertVisitHistoryInfoResponse(response.getData().getVisitHistory(), 1, 0, true);
        }

        @Test
        void ??????_??????_?????????_????????????_?????????_?????????_?????????_???????????????_?????????_????????????_??????_??????????????????_????????????() throws Exception {
            // given
            LocalDate startDate = LocalDate.of(2021, 10, 19);

            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            VisitHistory visitHistoryBeforeStartDate = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 18));
            VisitHistory visitHistoryAfterStartDate = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 19));
            visitHistoryRepository.saveAll(List.of(visitHistoryBeforeStartDate, visitHistoryAfterStartDate));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(startDate)
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getVisitHistories()).hasSize(1),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(0), visitHistoryAfterStartDate, user)
            );
        }

        @DisplayName("v3.0.6 ?????? ??????")
        @Test
        void ??????_??????_?????????_????????????_????????????_?????????_???????????????_?????????_??????() throws Exception {
            // given
            long notFoundUserId = -1L;

            LocalDate startDate = LocalDate.of(2021, 10, 19);

            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            VisitHistory visitHistory = VisitHistoryFixture.create(store, notFoundUserId, VisitType.EXISTS, LocalDate.of(2021, 10, 19));
            visitHistoryRepository.save(visitHistory);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(startDate)
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getVisitHistories()).hasSize(1),
                () -> assertThat(response.getData().getVisitHistories().get(0).getVisitHistoryId()).isEqualTo(visitHistory.getId()),
                () -> assertThat(response.getData().getVisitHistories().get(0).getType()).isEqualTo(visitHistory.getType()),
                () -> assertThat(response.getData().getVisitHistories().get(0).getUser().getUserId()).isNull(),
                () -> assertThat(response.getData().getVisitHistories().get(0).getUser().getName()).isEqualTo("????????? ?????????"),
                () -> assertThat(response.getData().getVisitHistories().get(0).getUser().getSocialType()).isNull()
            );
        }

        @DisplayName("??? ????????? ?????? ??????????????? startDate??? ????????? ????????? ?????? 7????????? ?????? ????????? ????????????")
        @Test
        void ??????_??????_?????????_???????????????_????????????_??????_??????_????????????_????????????() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreFixture.createWithDefaultMenu(user.getId());
            storeRepository.save(store);

            VisitHistory beforeLastWeekHistory = VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, today.minusWeeks(1).minusDays(1));
            VisitHistory lastWeekHistory = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, today.minusWeeks(1));
            VisitHistory todayHistory = VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, today);
            visitHistoryRepository.saveAll(List.of(beforeLastWeekHistory, lastWeekHistory, todayHistory));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getVisitHistories()).hasSize(2),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(0), todayHistory, user),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(1), lastWeekHistory, user)
            );
        }

    }

    @DisplayName("GET /api/v2/stores/me")
    @Nested
    class GetMyStoresApiTest {

        @Test
        void ??????_?????????_??????_??????_?????????_?????????_?????????_?????????_??????_?????????_N??????_??????_?????????_????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2");
            Store store3 = StoreFixture.createWithDefaultMenu(user.getId(), "??????3");

            storeRepository.saveAll(List.of(store1, store2, store3));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<StoresCursorResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getTotalElements()).isEqualTo(3),
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(store2.getId()),
                () -> assertThat(response.getData().isHasNext()).isTrue(),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(0), store3),
                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(1), store2)
            );
        }

        @Test
        void ??????_?????????_??????_??????_?????????_?????????_?????????_??????_??????_?????????_?????????_??????_????????????_N???_????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2");
            Store store3 = StoreFixture.createWithDefaultMenu(user.getId(), "??????3");
            Store store4 = StoreFixture.createWithDefaultMenu(user.getId(), "??????3");
            storeRepository.saveAll(List.of(store1, store2, store3, store4));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testBuilder()
                .size(2)
                .cursor(store4.getId())
                .build();

            // when
            ApiResponse<StoresCursorResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getTotalElements()).isEqualTo(4),
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(store2.getId()),
                () -> assertThat(response.getData().isHasNext()).isTrue(),
                () -> assertThat(response.getData().getContents()).hasSize(2),
                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(0), store3),
                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(1), store2)
            );
        }

        @DisplayName("????????? ?????????????????? nextCursor = -1")
        @Test
        void ??????_?????????_??????_??????_?????????_?????????_?????????_?????????_?????????_????????????_?????????_????????????_1_???_????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2");
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testBuilder()
                .size(1)
                .cursor(store2.getId())
                .build();

            // when
            ApiResponse<StoresCursorResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getTotalElements()).isEqualTo(2),
                () -> assertThat(response.getData().getContents()).hasSize(1)
            );
        }

        @DisplayName("????????? ?????????????????? nextCursor = -1")
        @Test
        void ??????_?????????_??????_??????_?????????_?????????_????????????_??????_?????????_????????????_?????????_??????????????????_????????????_????????????_1???_????????????() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "??????1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "??????2");
            Store store3 = StoreFixture.createWithDefaultMenu(user.getId(), "??????3");
            storeRepository.saveAll(List.of(store1, store2, store3));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testBuilder()
                .size(2)
                .cursor(store2.getId())
                .build();

            // when
            ApiResponse<StoresCursorResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getTotalElements()).isEqualTo(3),
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(0), store1)
            );
        }

        @Test
        void ??????_?????????_??????_??????_?????????_?????????_?????????_?????????_?????????_????????????() throws Exception {
            // given
            Store storeDeletedByUser = StoreFixture.createDefaultWithMenu(user.getId(), "?????? ??????", StoreStatus.DELETED);
            Store storeDeletedByAdmin = StoreFixture.createDefaultWithMenu(user.getId(), "?????? ??????", StoreStatus.FILTERED);
            storeRepository.saveAll(List.of(storeDeletedByUser, storeDeletedByAdmin));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<StoresCursorResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getTotalElements()).isEqualTo(2),
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(0), storeDeletedByAdmin.getId(), storeDeletedByAdmin.getLatitude(), storeDeletedByAdmin.getLongitude(), storeDeletedByAdmin.getName(), storeDeletedByAdmin.getRating()),
                () -> assertThat(response.getData().getContents().get(1).getCategories()).hasSize(1),
                () -> assertThat(response.getData().getContents().get(1).getCategories()).containsExactlyInAnyOrder(UserMenuCategoryType.BUNGEOPPANG),

                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(1), storeDeletedByUser.getId(), storeDeletedByUser.getLatitude(), storeDeletedByUser.getLongitude(), storeDeletedByUser.getName(), storeDeletedByUser.getRating()),
                () -> assertThat(response.getData().getContents().get(0).getCategories()).hasSize(1),
                () -> assertThat(response.getData().getContents().get(0).getCategories()).containsExactlyInAnyOrder(UserMenuCategoryType.BUNGEOPPANG)
            );
        }

        @Test
        void ??????_?????????_??????_??????_?????????_????????????_??????_??????_????????????_????????????() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "?????? ??????");
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.saveAll(List.of(
                VisitHistoryFixture.create(store, user.getId(), VisitType.EXISTS, today.minusMonths(1)),
                VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, today.minusWeeks(1)),
                VisitHistoryFixture.create(store, user.getId(), VisitType.NOT_EXISTS, today))
            );

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testBuilder()
                .size(1)
                .cursor(null)
                .build();

            // when
            ApiResponse<StoresCursorResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getContents()).hasSize(1),
                () -> assertVisitHistoryInfoResponse(response.getData().getContents().get(0).getVisitHistory(), 1, 2, false)
            );
        }

    }

    @DisplayName("GET /api/v1/stores/near/exists")
    @Nested
    class CheckExistsStoreAroundApiTest {

        @Test
        void ?????????_?????????_?????????_????????????_????????????_????????????_True???_????????????() throws Exception {
            // given
            Store store = StoreFixture.create(user.getId(), "????????? ??????", 34, 126, 1.1);
            store.addMenus(List.of(MenuFixture.create(store, "??? ?????????", "2?????? ??????", UserMenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            CheckExistsStoresNearbyRequest request = CheckExistsStoresNearbyRequest.testBuilder()
                .distance(2000.0)
                .build();

            // when
            ApiResponse<CheckExistStoresNearbyResponse> response = storeRetrieveMockApiCaller.checkExistStoresNearby(request, LocationValue.of(34, 126), 200);

            // then
            assertThat(response.getData().getIsExists()).isTrue();
        }

        @Test
        void ?????????_?????????_?????????_????????????_????????????_False???_????????????() throws Exception {
            // given
            CheckExistsStoresNearbyRequest request = CheckExistsStoresNearbyRequest.testBuilder()
                .distance(2000.0)
                .build();

            // when
            ApiResponse<CheckExistStoresNearbyResponse> response = storeRetrieveMockApiCaller.checkExistStoresNearby(request, LocationValue.of(34, 126), 200);

            // then
            assertThat(response.getData().getIsExists()).isFalse();
        }

    }

}
