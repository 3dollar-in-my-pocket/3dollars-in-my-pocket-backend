package com.depromeet.threedollar.api.userservice.controller.store;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        void 현재_위치_주변의_거리내_가게_목록을_조회합니다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "붕어빵 가게 1", 34, 126);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "붕어빵 가게 2", 34, 126);
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
        void 현재_위치_주변의_거리내_가게_목록을_조회시_메뉴_카테고리_목록도_반환된다() throws Exception {
            // given
            Store store = StoreFixture.create(user.getId(), "붕세권 가게", 34.0, 126.0);
            store.addMenus(List.of(
                MenuFixture.create(store, "팥붕", "2개 천원", UserMenuCategoryType.SUNDAE),
                MenuFixture.create(store, "슈붕", "2개 천원", UserMenuCategoryType.DALGONA)
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
        void 주변_가게_목록을_조회할때_삭제된_가게는_포함되지_않는다() throws Exception {
            // given
            Store deletedStoreByUser = StoreFixture.create(user.getId(), "타코야키 다 내꺼야", 34.0, 126.0, 1.0, StoreStatus.DELETED);
            Store deletedStoreByAdmin = StoreFixture.create(user.getId(), "타코야키 다 내꺼야", 34.0, 126.0, 1.0, StoreStatus.FILTERED);
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
        void 주변_가게_목록_조회시_한달내의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름", 34.0, 126.0);
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
        void 주변_가게들을_조회할때_한달이_지난_방문기록은_방문_카운트에_포함되지_않는다() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름", 34.0, 126.0);
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
        void 내_주변의_가게_목록을_조회할때_특정_메뉴_카테고리만_필터링해서_조회합니다() throws Exception {
            // given
            Store store1 = StoreFixture.create(user.getId(), "가게1", 34, 126);
            store1.addMenus(List.of(MenuFixture.create(store1, "메뉴2", "가격2", UserMenuCategoryType.DALGONA)));

            Store store2 = StoreFixture.create(user.getId(), "가게1", 34, 126);
            store2.addMenus(List.of(MenuFixture.create(store2, "메뉴2", "가격2", UserMenuCategoryType.BUNGEOPPANG)));

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
        void 내_주변의_가게_목록을_조회할때_정렬방식을_DISTANCE_ASC로_조회시_거리가_가까운_순으로_정렬해서_조회합니다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1", 34.00015, 126);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2", 34.0001, 126);
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
        void 내_주변의_가게_목록을_조회할때_정렬방식을_REVIEW_DESC로_조회시_평균_리뷰_점수가가_높은것부터_정렬해서_조회합니다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1", 34.00015, 126, 5);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2", 34.0001, 126, 1);
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
        void 내_주변의_가게를_조회할때_거리가_가까운_가게부터_최대_파라미터로_넘긴_SIZE_만큼_조회한다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1", 34.00015, 126, 5);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2", 34.0001, 126, 1);
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
        void 내_주변의_가게를_조회할때_평균_리뷰점수가_높은것부터_최대_파라미터로_넘긴_SIZE_만큼_조회한다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1", 34.00015, 126, 5);
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2", 34.0001, 126, 1);
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
        void 캐시에_주변_가게_데이터가_없는경우_DB에서_조회된_데이터들이_캐시에_저장된다() throws Exception {
            // given
            double latitude = 34.0;
            double longitude = 126.0;
            double distance = 1000;

            when(aroundUserStoresCacheRepository.getCache(anyDouble(), anyDouble(), anyDouble())).thenReturn(null);

            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "붕어빵 가게 1", latitude, longitude);
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
        void 캐시에_주변_가게_데이터가_있다면_DB_조회없이_캐시에서_조회해서_반환한다() throws Exception {
            // given
            double latitude = 34.0;
            double longitude = 126.0;
            double distance = 1000;

            UserStoreCacheModel cachedStore = UserStoreCacheModel.of(
                List.of(UserMenuCategoryType.SUNDAE, UserMenuCategoryType.WAFFLE),
                100L,
                latitude,
                longitude,
                "가게 이름",
                3.4,
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0)
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
        void 캐시에_있는_가게들중_카테고리를_필터링해서_반환한다() throws Exception {
            // given
            double latitude = 35.0;
            double longitude = 127.0;
            double distance = 1000;

            UserStoreCacheModel noMatchedStore = UserStoreCacheModel.of(
                List.of(UserMenuCategoryType.SUNDAE, UserMenuCategoryType.WAFFLE),
                100L,
                latitude,
                longitude,
                "가게 이름",
                3.4,
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0)
            );
            UserStoreCacheModel matchedStore = UserStoreCacheModel.of(
                List.of(UserMenuCategoryType.SUNDAE, UserMenuCategoryType.BUNGEOPPANG),
                100L,
                latitude,
                longitude,
                "가게 이름",
                3.4,
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0)
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
        void 가게_상세_정보를_조회한다() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 가게_상세_정보_조회시_메뉴_정보도_함께_조회된다() throws Exception {
            // given
            Store store = StoreFixture.create(user.getId(), "가게 이름");
            Menu menu1 = MenuFixture.create(store, "메뉴1", "가격1", UserMenuCategoryType.BUNGEOPPANG);
            Menu menu2 = MenuFixture.create(store, "메뉴2", "가격2", UserMenuCategoryType.GUKWAPPANG);
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
        void 가게_상세_정보_조회시_제보자_정보도_함께_반환된다() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 가게_상세_정보_조회시_영업일_정보도_함께_반환된다() throws Exception {
            // given
            Set<DayOfTheWeek> dayOfTheWeeks = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY, DayOfTheWeek.THURSDAY);
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 가게_상세_정보_조회시_결제_방법_정보도_함께_반환된다() throws Exception {
            // given
            Set<PaymentMethodType> paymentMethodTypes = Set.of(PaymentMethodType.CASH, PaymentMethodType.CARD);
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 가게_상세_정보_조회시_회원탈퇴한_제보자인경우_사라진_제보자로_표기된다() throws Exception {
            // given
            long notFoundUserId = -1L;

            Store store = StoreFixture.createWithDefaultMenu(notFoundUserId, "가게 이름");
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.retrieveStoreDetailInfo(request, LocationValue.of(34.0, 126.0), token, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertUserInfoResponse(data.getUser(), null, "사라진 제보자", null);
        }

        @Test
        void 가게_상세_정보_조회시_가게에_등록된_이미지_목록도_조회한다() throws Exception {
            // given
            String imageUrl = "https://image.url";

            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 가게_상세_정보_조회시_삭제된_이미지는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
            storeRepository.save(store);

            StoreImage storeImage = StoreImageFixture.create(store.getId(), user.getId(), "https://store-image.com");
            storeImage.delete();
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
        void 가게_상세_조회시_리뷰와_리뷰_작성자_정보가_최신것부터_정렬되서_조회된다() throws Exception {
            // given
            long notFoundUserId = -1L;

            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
            storeRepository.save(store);

            Review review1 = ReviewFixture.create(store.getId(), user.getId(), "댓글 1", 5);
            Review review2 = ReviewFixture.create(store.getId(), notFoundUserId, "댓글 2", 3);

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
                () -> assertUserInfoResponse(data.getReviews().get(0).getUser(), null, "사라진 제보자", null),

                () -> assertReviewWithWriterResponse(data.getReviews().get(1), review1),
                () -> assertUserInfoResponse(data.getReviews().get(1).getUser(), user)
            );
        }

        @Test
        void 가게_상세_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 가게_상세_조회시_기간내에_가게에_방문한_기록과_유저정보를_방문한_기록들을_최근_생성된것부터_조회한다() throws Exception {
            // given
            LocalDate startDate = LocalDate.of(2021, 10, 19);

            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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

        @DisplayName("v3.0.6 버그 수정")
        @Test
        void 가게_상세_조회시_한달간의_방문기록_정보가_회원탈퇴한_유저의_경우() throws Exception {
            // given
            long notFoundUserId = -1L;

            LocalDate startDate = LocalDate.of(2021, 10, 19);

            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
                () -> assertThat(response.getData().getVisitHistories().get(0).getUser().getName()).isEqualTo("사라진 제보자"),
                () -> assertThat(response.getData().getVisitHistories().get(0).getUser().getSocialType()).isNull()
            );
        }

        @DisplayName("앱 호환을 위해 기본적으로 startDate를 넘기지 않으면 지난 7일간의 방문 기록을 조회한다")
        @Test
        void 가게_상세_조회시_기본적으로_일주일간_방문_인증_기록들을_조회한다() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 내가_작성한_가게_목록_조회시_커서를_넘기지_않으면_가장_최신의_N개의_가게_정보를_반환한다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2");
            Store store3 = StoreFixture.createWithDefaultMenu(user.getId(), "가게3");

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
        void 내가_작성한_가게_목록_조회시_커서를_넘기면_해당_커서_이전에_생성된_가게_목록들이_N개_반환한다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2");
            Store store3 = StoreFixture.createWithDefaultMenu(user.getId(), "가게3");
            Store store4 = StoreFixture.createWithDefaultMenu(user.getId(), "가게3");
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

        @DisplayName("마지막 페이지인경우 nextCursor = -1")
        @Test
        void 내가_작성한_가게_목록_조회시_이후에_더이상_조회할_가게가_없을경우_커서를_마이너스_1_로_반환환다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2");
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

        @DisplayName("마지막 페이지인경우 nextCursor = -1")
        @Test
        void 내가_작성한_가게_목록_조회시_요청한_개수보다_적은_가게를_반환하면_마지막_스크롤이라고_판단하고_마이너스_1을_반환한다() throws Exception {
            // given
            Store store1 = StoreFixture.createWithDefaultMenu(user.getId(), "가게1");
            Store store2 = StoreFixture.createWithDefaultMenu(user.getId(), "가게2");
            Store store3 = StoreFixture.createWithDefaultMenu(user.getId(), "가게3");
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
        void 내가_작성한_가게_목록_조회시_삭제된_가게는_삭제된_가게로_조회된다() throws Exception {
            // given
            Store storeDeletedByUser = StoreFixture.createDefaultWithMenu(user.getId(), "가게 이름", StoreStatus.DELETED);
            Store storeDeletedByAdmin = StoreFixture.createDefaultWithMenu(user.getId(), "가게 이름", StoreStatus.FILTERED);
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
        void 내가_작성한_가게_목록_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            Store store = StoreFixture.createWithDefaultMenu(user.getId(), "가게 이름");
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
        void 주변에_가게가_있는지_조회한다_하나라도_있는경우_True를_반환한다() throws Exception {
            // given
            Store store = StoreFixture.create(user.getId(), "붕어빵 가게", 34, 126, 1.1);
            store.addMenus(List.of(MenuFixture.create(store, "팥 붕어빵", "2개에 천원", UserMenuCategoryType.BUNGEOPPANG)));
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
        void 주변에_가게가_있는지_조회한다_없는경우_False를_반환한다() throws Exception {
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
