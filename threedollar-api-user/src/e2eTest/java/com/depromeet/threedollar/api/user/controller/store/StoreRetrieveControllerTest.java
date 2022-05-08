package com.depromeet.threedollar.api.user.controller.store;

import static com.depromeet.threedollar.api.user.controller.review.support.ReviewAssertions.assertReviewWithWriterResponse;
import static com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions.assertMenuResponse;
import static com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions.assertStoreDetailInfoResponse;
import static com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions.assertStoreWithVisitsAndDistanceResponse;
import static com.depromeet.threedollar.api.user.controller.store.support.StoreAssertions.assertStoreWithVisitsResponse;
import static com.depromeet.threedollar.api.user.controller.store.support.StoreImageAssertions.assertStoreImageResponse;
import static com.depromeet.threedollar.api.user.controller.user.support.UserAssertions.assertUserInfoResponse;
import static com.depromeet.threedollar.api.user.controller.visit.support.VisitHistoryAssertions.assertVisitHistoryInfoResponse;
import static com.depromeet.threedollar.api.user.controller.visit.support.VisitHistoryAssertions.assertVisitHistoryWithUserResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.user.service.store.dto.request.CheckExistsStoresNearbyRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.CheckExistStoresNearbyResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoresCursorResponse;
import com.depromeet.threedollar.api.user.service.store.dto.type.UserStoreOrderType;
import com.depromeet.threedollar.common.model.CoordinateValue;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.Menu;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreWithMenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;

class StoreRetrieveControllerTest extends SetupUserControllerTest {

    private StoreRetrieveMockApiCaller storeRetrieveMockApiCaller;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AppearanceDayRepository appearanceDayRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @BeforeEach
    void setUp() {
        storeRetrieveMockApiCaller = new StoreRetrieveMockApiCaller(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        storeImageRepository.deleteAllInBatch();
        reviewRepository.deleteAllInBatch();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        visitHistoryRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @DisplayName("GET /api/v2/stores/near")
    @Nested
    class GetAroundStoresApiTest {

        @Test
        void 나의_지도상_주변_가게들을_조회한다() throws Exception {
            // given
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름1")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름2")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertStoreWithVisitsAndDistanceResponse(response.getData().get(0), store1.getId(), store1.getLatitude(), store1.getLongitude(), store1.getName(), store1.getRating()),
                () -> assertStoreWithVisitsAndDistanceResponse(response.getData().get(1), store2.getId(), store2.getLatitude(), store2.getLongitude(), store2.getName(), store2.getRating())
            );
        }

        @Test
        void 주변_가게들을_조회할때_메뉴_카테고리_목록도_반환된다() throws Exception {
            // given
            Store store = StoreCreator.builder()
                .userId(user.getId())
                .storeName("행복한 잉어빵")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            store.addMenus(List.of(
                MenuCreator.builder()
                    .store(store)
                    .name("순대")
                    .price("1개 1000원")
                    .category(MenuCategoryType.SUNDAE)
                    .build(),
                MenuCreator.builder()
                    .store(store)
                    .name("달고나")
                    .price("2개 1000원")
                    .category(MenuCategoryType.DALGONA)
                    .build()
            ));
            storeRepository.save(store);

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),

                () -> assertThat(response.getData().get(0).getCategories()).hasSize(2),
                () -> assertThat(response.getData().get(0).getCategories()).containsExactlyInAnyOrder(MenuCategoryType.SUNDAE, MenuCategoryType.DALGONA)
            );
        }

        @Test
        void 주변_가게들을_조회할때_삭제된_가게는_포함되지_않는다() throws Exception {
            // given
            Store deletedStoreByUser = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("유저에 의해 삭제된 가게의 이름")
                .latitude(34.0)
                .longitude(126.0)
                .rating(1.0)
                .status(StoreStatus.DELETED)
                .build();
            Store deletedStoreByAdmin = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("관리자에 의해 삭제된 가게의 이름")
                .latitude(34.0)
                .longitude(126.0)
                .rating(3.0)
                .status(StoreStatus.FILTERED)
                .build();
            storeRepository.saveAll(List.of(deletedStoreByUser, deletedStoreByAdmin));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertThat(response.getData()).isEmpty();
        }

        @Test
        void 주변_가게_목록_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.NOT_EXISTS)
                    .dateOfVisit(today.minusMonths(1))
                    .build(),
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.EXISTS)
                    .dateOfVisit(today.minusWeeks(1))
                    .build(),
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.EXISTS)
                    .dateOfVisit(today)
                    .build()
            ));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().get(0).getVisitHistory(), 2, 1, true);
        }

        @Test
        void 주변_가게들을_조회할때_한달이_지난_방문기록은_방문_카운트에_포함되지_않는다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            VisitHistory visitHistory = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.NOT_EXISTS)
                .dateOfVisit(today.minusMonths(1).minusDays(1))
                .build();
            visitHistoryRepository.save(visitHistory);

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertVisitHistoryInfoResponse(response.getData().get(0).getVisitHistory(), 0, 0, false)
            );
        }

        @Test
        void 주변_가게들을_조회할때_카테고리를_넘기면_해당_카테고리_메뉴를_판매하는_가게들_내에서_조회된다() throws Exception {
            // given
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름 1")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            store1.addMenus(List.of(
                    MenuCreator.builder()
                        .store(store1)
                        .name("달고나")
                        .price("1개 1000원")
                        .category(MenuCategoryType.DALGONA)
                        .build()
                )
            );
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름 2")
                .latitude(34.0)
                .longitude(126.0)
                .build();
            store2.addMenus(List.of(
                    MenuCreator.builder()
                        .store(store2)
                        .name("팥 붕어빵")
                        .price("5개 2000원")
                        .category(MenuCategoryType.BUNGEOPPANG)
                        .build()
                )
            );
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(1000)
                .category(MenuCategoryType.DALGONA)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId())
            );
        }

        @Test
        void 주변_가게들을_거리가_가까운_순으로_정렬해서_조회한다_DISTANCE_ASC() throws Exception {
            // given
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름1")
                .latitude(34.00015)
                .longitude(126.0)
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("뿡어빵 가게 이름2")
                .latitude(34.0001)
                .longitude(126.0)
                .build();
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(2000)
                .orderType(UserStoreOrderType.DISTANCE_ASC)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store2.getId()),
                () -> assertThat(response.getData().get(1).getStoreId()).isEqualTo(store1.getId())
            );
        }

        @Test
        void 주변_가게들을_리뷰_평점이_높은순으로_정렬해서_조회한다_REVIEW_DESC() throws Exception {
            // given
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름1")
                .latitude(34.00015)
                .longitude(126.0)
                .rating(5.0)
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름2")
                .latitude(34.0001)
                .longitude(126.0)
                .rating(1.0)
                .build();
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .distance(2000)
                .orderType(UserStoreOrderType.REVIEW_DESC)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, CoordinateValue.of(34.0, 126.0), CoordinateValue.of(34.0, 126.0), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId()),
                () -> assertThat(response.getData().get(1).getStoreId()).isEqualTo(store2.getId())
            );
        }

    }

    @DisplayName("GET /api/v2/store")
    @Nested
    class GetStoreDetailApiTest {

        @Test
        void 특정_가게에_대한_상세_가게_정보를_조회한다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름")
                .build();
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
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertStoreDetailInfoResponse(data, store, user);
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할때_메뉴_목록_에_대한_정보가_반환된다() throws Exception {
            // given
            Store store = StoreCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름")
                .build();
            Menu menu1 = MenuCreator.builder()
                .store(store)
                .name("팥 붕어빵")
                .price("2개 1000원")
                .category(MenuCategoryType.BUNGEOPPANG)
                .build();
            Menu menu2 = MenuCreator.builder()
                .store(store)
                .name("팥 붕어빵")
                .price("5개 2000원")
                .category(MenuCategoryType.GUKWAPPANG)
                .build();
            store.addMenus(List.of(menu1, menu2));
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getCategories()).containsExactlyInAnyOrderElementsOf(List.of(MenuCategoryType.BUNGEOPPANG, MenuCategoryType.GUKWAPPANG)),
                () -> assertThat(response.getData().getMenus()).hasSize(2),
                () -> assertMenuResponse(response.getData().getMenus().get(0), menu1),
                () -> assertMenuResponse(response.getData().getMenus().get(1), menu2)
            );
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할떄_제보자_정보도_함께_반환된다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름")
                .build();
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertUserInfoResponse(response.getData().getUser(), user);
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할떄_개장일_정보도_반환된다() throws Exception {
            // given
            Set<DayOfTheWeek> dayOfTheWeeks = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY, DayOfTheWeek.THURSDAY);
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름")
                .build();
            store.addAppearanceDays(dayOfTheWeeks);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getAppearanceDays()).hasSize(dayOfTheWeeks.size()),
                () -> assertThat(response.getData().getAppearanceDays()).containsExactlyInAnyOrderElementsOf(dayOfTheWeeks)
            );
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할때_결제방법_정보도_반환된다() throws Exception {
            // given
            Set<PaymentMethodType> paymentMethodTypes = Set.of(PaymentMethodType.CASH, PaymentMethodType.CARD);
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름")
                .build();
            store.addPaymentMethods(paymentMethodTypes);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getPaymentMethods()).hasSize(paymentMethodTypes.size()),
                () -> assertThat(response.getData().getPaymentMethods()).containsExactlyInAnyOrderElementsOf(paymentMethodTypes)
            );
        }

        @Test
        void 제보자가_회원탈퇴했을경우_사라진_제보자라고_표기된다() throws Exception {
            // given
            long notFoundUserId = -1L;

            Store store = StoreWithMenuCreator.builder()
                .userId(notFoundUserId)
                .storeName("붕어빵 가게 이름")
                .build();
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertUserInfoResponse(data.getUser(), null, "사라진 제보자", null);
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회한다() throws Exception {
            // given
            String imageUrl = "https://image.url";

            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름")
                .build();
            storeRepository.save(store);

            StoreImage storeImage = StoreImage.newInstance(store, user.getId(), imageUrl);
            storeImageRepository.save(storeImage);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getImages()).hasSize(1),
                () -> assertStoreImageResponse(response.getData().getImages().get(0), storeImage.getId(), imageUrl)
            );
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회시_삭제된_이미지는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름")
                .build();
            storeRepository.save(store);

            StoreImage storeImage = StoreImage.newInstance(store, user.getId(), "https://store-image.com");
            storeImage.delete();
            storeImageRepository.save(storeImage);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertThat(response.getData().getImages()).isEmpty();
        }

        @Test
        void 가게_상세조회시_작성된_리뷰_와_작성자_정보가_최근_생성순으로_조회된다() throws Exception {
            // given
            long notFoundUserId = -1L;

            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("토끼의 붕어빵")
                .build();
            storeRepository.save(store);

            Review review1 = ReviewCreator.builder()
                .storeId(store.getId())
                .userId(user.getId())
                .contents("맛있어요")
                .rating(5)
                .build();
            Review review2 = ReviewCreator.builder()
                .storeId(store.getId())
                .userId(notFoundUserId)
                .contents("그냥 그래요")
                .rating(3)
                .build();
            reviewRepository.saveAll(List.of(review1, review2));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

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
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("토끼의 붕어빵 판매점")
                .build();
            storeRepository.save(store);

            VisitHistory visitHistory = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.EXISTS)
                .dateOfVisit(today.minusMonths(1))
                .build();
            visitHistoryRepository.save(visitHistory);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertVisitHistoryInfoResponse(response.getData().getVisitHistory(), 1, 0, true);
        }

        @Test
        void 가게_상세_조회시_기간내에_가게에_방문한_기록과_유저정보를_방문한_기록들을_최근_생성된것부터_조회한다() throws Exception {
            // given
            LocalDate startDate = LocalDate.of(2021, 10, 19);

            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("토끼의 붕세권")
                .build();
            storeRepository.save(store);

            VisitHistory visitHistoryBeforeStartDate = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.EXISTS)
                .dateOfVisit(LocalDate.of(2021, 10, 18))
                .build();
            VisitHistory visitHistoryAfterStartDate = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.EXISTS)
                .dateOfVisit(LocalDate.of(2021, 10, 19))
                .build();
            visitHistoryRepository.saveAll(List.of(visitHistoryBeforeStartDate, visitHistoryAfterStartDate));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(startDate)
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getVisitHistories()).hasSize(1),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(0), visitHistoryAfterStartDate, user)
            );
        }

        @DisplayName("앱 호환을 위해 기본적으로 startDate를 넘기지 않으면 지난 7일간의 방문 기록을 조회한다")
        @Test
        void 가게_상세_조회시_기본적으로_일주일간_방문_인증_기록들을_조회한다() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("토끼와 함께하는 붕어빵")
                .build();
            storeRepository.save(store);

            VisitHistory beforeLastWeekHistory = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.NOT_EXISTS)
                .dateOfVisit(today.minusWeeks(1).minusDays(1))
                .build();
            VisitHistory lastWeekHistory = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.EXISTS)
                .dateOfVisit(today.minusWeeks(1))
                .build();
            VisitHistory todayHistory = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.EXISTS)
                .dateOfVisit(today)
                .build();
            visitHistoryRepository.saveAll(List.of(beforeLastWeekHistory, lastWeekHistory, todayHistory));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testBuilder()
                .storeId(store.getId())
                .startDate(LocalDate.now().minusWeeks(1))
                .build();

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, CoordinateValue.of(34.0, 126.0), token, 200);

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
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름 1")
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름 2")
                .build();
            Store store3 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름 3")
                .build();
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
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름1")
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름2")
                .build();
            Store store3 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름3")
                .build();
            Store store4 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름4")
                .build();
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
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름1")
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("붕어빵 가게 이름2")
                .build();
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
            Store store1 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("노점상1")
                .build();
            Store store2 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("노점상2")
                .build();
            Store store3 = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("노점상3")
                .build();
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
            Store storeDeletedByUser = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름1")
                .status(StoreStatus.DELETED)
                .build();
            Store storeDeletedByAdmin = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름2")
                .status(StoreStatus.FILTERED)
                .build();
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
                () -> assertThat(response.getData().getContents().get(1).getCategories()).containsExactlyInAnyOrder(MenuCategoryType.BUNGEOPPANG),

                () -> assertStoreWithVisitsResponse(response.getData().getContents().get(1), storeDeletedByUser.getId(), storeDeletedByUser.getLatitude(), storeDeletedByUser.getLongitude(), storeDeletedByUser.getName(), storeDeletedByUser.getRating()),
                () -> assertThat(response.getData().getContents().get(0).getCategories()).hasSize(1),
                () -> assertThat(response.getData().getContents().get(0).getCategories()).containsExactlyInAnyOrder(MenuCategoryType.BUNGEOPPANG)
            );
        }

        @Test
        void 내가_작성한_가게_목록_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름1")
                .build();
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.saveAll(List.of(
                    VisitHistoryCreator.builder()
                        .store(store)
                        .userId(user.getId())
                        .type(VisitType.EXISTS)
                        .dateOfVisit(today.minusMonths(1))
                        .build(),
                    VisitHistoryCreator.builder()
                        .store(store)
                        .userId(user.getId())
                        .type(VisitType.NOT_EXISTS)
                        .dateOfVisit(today.minusWeeks(1))
                        .build(),
                    VisitHistoryCreator.builder()
                        .store(store)
                        .userId(user.getId())
                        .type(VisitType.NOT_EXISTS)
                        .dateOfVisit(today)
                        .build()
                )
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
        void 주변에_가게가_존재하는_경우_return_True() throws Exception {
            // given
            Store store = StoreWithMenuCreator.builder()
                .userId(user.getId())
                .storeName("가게 이름1")
                .latitude(34.0)
                .longitude(126.0)
                .rating(1.1)
                .build();
            store.addMenus(List.of(
                MenuCreator.builder()
                    .store(store)
                    .name("팥 붕어빵")
                    .price("2개에 천원")
                    .category(MenuCategoryType.BUNGEOPPANG)
                    .build()
            ));
            storeRepository.save(store);

            CheckExistsStoresNearbyRequest request = CheckExistsStoresNearbyRequest.testBuilder()
                .distance(2000.0)
                .build();

            // when
            ApiResponse<CheckExistStoresNearbyResponse> response = storeRetrieveMockApiCaller.checkExistStoresNearby(request, CoordinateValue.of(34, 126), 200);

            // then
            assertThat(response.getData().getIsExists()).isTrue();
        }

        @Test
        void 주변에_어떤_가게도_존재하지_않으면_return_False() throws Exception {
            // given
            CheckExistsStoresNearbyRequest request = CheckExistsStoresNearbyRequest.testBuilder()
                .distance(2000.0)
                .build();

            // when
            ApiResponse<CheckExistStoresNearbyResponse> response = storeRetrieveMockApiCaller.checkExistStoresNearby(request, CoordinateValue.of(34, 126), 200);

            // then
            assertThat(response.getData().getIsExists()).isFalse();
        }

    }

}
