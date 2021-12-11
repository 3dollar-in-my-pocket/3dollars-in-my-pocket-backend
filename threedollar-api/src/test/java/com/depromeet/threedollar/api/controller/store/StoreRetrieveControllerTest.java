package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.store.dto.request.*;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveMyStoresV2Request;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveStoreGroupByCategoryV2Request;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByDistanceV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByReviewV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresScrollV2Response;
import com.depromeet.threedollar.api.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.depromeet.threedollar.api.assertutils.assertReviewUtils.assertReviewWithWriterResponse;
import static com.depromeet.threedollar.api.assertutils.assertStoreImageUtils.assertStoreImageResponse;
import static com.depromeet.threedollar.api.assertutils.assertStoreUtils.*;
import static com.depromeet.threedollar.api.assertutils.assertUserUtils.assertUserInfoResponse;
import static com.depromeet.threedollar.api.assertutils.assertVisitHistoryUtils.assertVisitHistoryInfoResponse;
import static com.depromeet.threedollar.api.assertutils.assertVisitHistoryUtils.assertVisitHistoryWithUserResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreRetrieveControllerTest extends SetupUserControllerTest {

    private StoreRetrieveMockApiCaller storeRetrieveMockApiCaller;

    @BeforeEach
    void setUp() {
        storeRetrieveMockApiCaller = new StoreRetrieveMockApiCaller(mockMvc, objectMapper);
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
    private ReviewRepository reviewRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Autowired
    private StoreImageRepository storeImageRepository;

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
    class 주변의_가게_조회 {

        @Test
        void 나의_지도상_주변_가게들을_조회한다() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름 1", 34, 126);
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름 2", 34, 126);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertStoreWithVisitsAndDistanceResponse(response.getData().get(0), store1.getId(), store1.getLatitude(), store1.getLongitude(), store1.getName(), store1.getRating());
            assertStoreWithVisitsAndDistanceResponse(response.getData().get(1), store2.getId(), store2.getLatitude(), store2.getLongitude(), store2.getName(), store2.getRating());
        }

        @Test
        void 주변_가게들을_조회할때_메뉴_카테고리_목록도_반환된다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게 이름", 34.0, 126.0);
            store.addMenus(List.of(
                MenuCreator.create(store, "메뉴 1", "가격 1", MenuCategoryType.SUNDAE),
                MenuCreator.create(store, "메뉴 1", "가격 1", MenuCategoryType.DALGONA)
            ));
            storeRepository.save(store);

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(1);

            assertThat(response.getData().get(0).getCategories()).hasSize(2);
            assertThat(response.getData().get(0).getCategories()).containsExactlyInAnyOrder(MenuCategoryType.SUNDAE, MenuCategoryType.DALGONA);
        }

        @Test
        void 주변_가게들을_조회할때_삭제된_가게는_포함되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.createDeletedWithDefaultMenu(testUser.getId(), "가게 이름", 34.0, 126.0);
            storeRepository.save(store);

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).isEmpty();
        }

        @Test
        void 주변_가게_목록_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름", 34.0, 126.0);
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, today.minusMonths(1)),
                VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, today.minusWeeks(1)),
                VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, today)
            ));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().get(0).getVisitHistory(), 2, 1, true);
        }

        @Test
        void 주변_가게들을_조회할때_한달이_지난_방문기록은_방문_카운트에_포함되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름", 34.0, 126.0);
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, today.minusMonths(1).minusDays(1)));
            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(1000)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().get(0).getVisitHistory(), 0, 0, false);
        }

        @Test
        void 주변_가게들을_조회할때_카테고리를_넘기면_해당_카테고리_메뉴를_판매하는_가게들_내에서_조회된다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 126);
            store1.addMenus(List.of(MenuCreator.create(store1, "메뉴2", "가격2", MenuCategoryType.DALGONA)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게1", 34, 126);
            store2.addMenus(List.of(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(1000)
                .category(MenuCategoryType.DALGONA)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId());
        }

        @Test
        void 주변_가게들을_거리가_가까운_순으로_정렬해서_조회한다_DISTANCE_ASC() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게1", 34.00015, 126);
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게2", 34.0001, 126);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(2000)
                .orderType(StoreOrderType.DISTANCE_ASC)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertThat(response.getData().get(0).getStoreId()).isEqualTo(store2.getId());
            assertThat(response.getData().get(1).getStoreId()).isEqualTo(store1.getId());
        }

        @Test
        void 주변_가게들을_리뷰_평점이_높은순으로_정렬해서_조회한다_REVIEW_DESC() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게1", 34.00015, 126, 5);
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게2", 34.0001, 126, 1);
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testBuilder()
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .distance(2000)
                .orderType(StoreOrderType.REVIEW_DESC)
                .build();

            // when
            ApiResponse<List<StoreWithVisitsAndDistanceResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertThat(response.getData().get(0).getStoreId()).isEqualTo(store1.getId());
            assertThat(response.getData().get(1).getStoreId()).isEqualTo(store2.getId());
        }

    }

    @DisplayName("GET /api/v2/store")
    @Nested
    class 가게_상세_정보_조회 {

        @Test
        void 특정_가게에_대한_상세_가게_정보를_조회한다() throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            DayOfTheWeek day = DayOfTheWeek.FRIDAY;
            store.addAppearanceDays(Set.of(day));

            PaymentMethodType paymentMethodType = PaymentMethodType.CASH;
            store.addPaymentMethods(Set.of(paymentMethodType));

            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertStoreDetailInfoResponse(data, store, testUser);
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할때_메뉴_목록_에_대한_정보가_반환된다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게 이름");
            Menu menu1 = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            Menu menu2 = MenuCreator.create(store, "메뉴2", "가격2", MenuCategoryType.GUKWAPPANG);
            store.addMenus(List.of(menu1, menu2));
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertThat(response.getData().getCategories()).containsExactlyInAnyOrderElementsOf(List.of(MenuCategoryType.BUNGEOPPANG, MenuCategoryType.GUKWAPPANG));

            assertThat(response.getData().getMenus()).hasSize(2);
            assertMenuResponse(response.getData().getMenus().get(0), menu1);
            assertMenuResponse(response.getData().getMenus().get(1), menu2);
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할떄_제보자_정보도_함께_반환된다() throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertUserInfoResponse(response.getData().getUser(), testUser);
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할떄_개장일_정보도_반환된다() throws Exception {
            // given
            Set<DayOfTheWeek> dayOfTheWeeks = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY, DayOfTheWeek.THURSDAY);
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            store.addAppearanceDays(dayOfTheWeeks);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertThat(response.getData().getAppearanceDays()).hasSize(dayOfTheWeeks.size());
            assertThat(response.getData().getAppearanceDays()).containsExactlyInAnyOrderElementsOf(dayOfTheWeeks);
        }

        @Test
        void 특정_가게에_대한_상세_정보를_조회할때_결제방법_정보도_반환된다() throws Exception {
            // given
            Set<PaymentMethodType> paymentMethodTypes = Set.of(PaymentMethodType.CASH, PaymentMethodType.CARD);
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            store.addPaymentMethods(paymentMethodTypes);
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertThat(response.getData().getPaymentMethods()).hasSize(paymentMethodTypes.size());
            assertThat(response.getData().getPaymentMethods()).containsExactlyInAnyOrderElementsOf(paymentMethodTypes);
        }

        @Test
        void 제보자가_회원탈퇴했을경우_사라진_제보자라고_표기된다() throws Exception {
            // given
            Long notFoundUserId = -1L;

            Store store = StoreCreator.createWithDefaultMenu(notFoundUserId, "가게 이름");
            storeRepository.save(store);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertUserInfoResponse(data.getUser(), null, "사라진 제보자", null);
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회한다() throws Exception {
            // given
            String imageUrl = "https://image.url";

            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            StoreImage storeImage = StoreImage.newInstance(store, testUser.getId(), imageUrl);
            storeImageRepository.save(storeImage);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getImages()).hasSize(1),
                () -> assertStoreImageResponse(response.getData().getImages().get(0), storeImage.getId(), imageUrl)
            );
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회시_삭제된_이미지는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            StoreImage storeImage = StoreImage.newInstance(store, testUser.getId(), "https://store-image.com");
            storeImage.delete();
            storeImageRepository.save(storeImage);

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertThat(response.getData().getImages()).isEmpty();
        }

        @Test
        void 가게_상세조회시_작성된_리뷰_와_작성자_정보가_최근_생성순으로_조회된다() throws Exception {
            // given
            Long notFoundUserId = -1L;

            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "댓글 1", 5);
            Review review2 = ReviewCreator.create(store.getId(), notFoundUserId, "댓글 2", 3);

            reviewRepository.saveAll(List.of(review1, review2));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertThat(data.getReviews()).hasSize(2);
            assertReviewWithWriterResponse(data.getReviews().get(0), review2);
            assertUserInfoResponse(data.getReviews().get(0).getUser(), null, "사라진 제보자", null);

            assertReviewWithWriterResponse(data.getReviews().get(1), review1);
            assertUserInfoResponse(data.getReviews().get(1).getUser(), testUser);
        }

        @Test
        void 가게_상세_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, today.minusMonths(1)));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertVisitHistoryInfoResponse(response.getData().getVisitHistory(), 1, 0, true);
        }

        @Test
        void 가게_상세_조회시_기간내에_가게에_방문한_기록과_유저정보를_방문한_기록들을_최근_생성된것부터_조회한다() throws Exception {
            // given
            LocalDate startDate = LocalDate.of(2021, 10, 19);

            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            VisitHistory visitHistoryBeforeStartDate = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 18));
            VisitHistory visitHistoryAfterStartDate = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 19));
            visitHistoryRepository.saveAll(List.of(visitHistoryBeforeStartDate, visitHistoryAfterStartDate));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId(), startDate);

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getVisitHistories()).hasSize(1),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(0), visitHistoryAfterStartDate, testUser)
            );
        }

        @DisplayName("앱 호환을 위해 기본적으로 startDate를 넘기지 않으면 지난 7일간의 방문 기록을 조회한다")
        @Test
        void 가게_상세_조회시_기본적으로_일주일간_방문_인증_기록들을_조회한다() throws Exception {
            // given
            LocalDate today = LocalDate.now();
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            VisitHistory beforeLastWeekHistory = VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, today.minusWeeks(1).minusDays(1));
            VisitHistory lastWeekHistory = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, today.minusWeeks(1));
            VisitHistory todayHistory = VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, today);
            visitHistoryRepository.saveAll(List.of(beforeLastWeekHistory, lastWeekHistory, todayHistory));

            RetrieveStoreDetailRequest request = RetrieveStoreDetailRequest.testInstance(store.getId());

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getVisitHistories()).hasSize(2),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(0), todayHistory, testUser),
                () -> assertVisitHistoryWithUserResponse(response.getData().getVisitHistories().get(1), lastWeekHistory, testUser)
            );
        }

    }

    @DisplayName("GET /api/v2/stores/me")
    @Nested
    class 내가_제보한_가게_목록_조회 {

        @Test
        void 내가_작성한_가게_목록_조회시_커서를_넘기지_않으면_가장_최신의_N개의_가게_정보를_반환한다() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게1");
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게2");
            Store store3 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게3");

            storeRepository.saveAll(List.of(store1, store2, store3));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, null);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(3);
            assertThat(response.getData().getNextCursor()).isEqualTo(store2.getId());
            assertThat(response.getData().getContents()).hasSize(2);

            assertStoreWithVisitsResponse(response.getData().getContents().get(0), store3);
            assertStoreWithVisitsResponse(response.getData().getContents().get(1), store2);
        }

        @Test
        void 내가_작성한_가게_목록_조회시_커서를_넘기면_해당_커서_이전에_생성된_가게_목록들이_N개_반환한다() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게1");
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게2");
            Store store3 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게3");
            Store store4 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게3");
            storeRepository.saveAll(List.of(store1, store2, store3, store4));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, store4.getId());

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(4);
            assertThat(response.getData().getNextCursor()).isEqualTo(store2.getId());
            assertThat(response.getData().getContents()).hasSize(2);
            assertStoreWithVisitsResponse(response.getData().getContents().get(0), store3);
            assertStoreWithVisitsResponse(response.getData().getContents().get(1), store2);
        }

        @DisplayName("마지막 페이지인경우 nextCursor = -1")
        @Test
        void 내가_작성한_가게_목록_조회시_이후에_더이상_조회할_가게가_없을경우_커서를_마이너스_1_로_반환환다() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게1");
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게2");
            storeRepository.saveAll(List.of(store1, store2));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(1, store2.getId());

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getTotalElements()).isEqualTo(2);
            assertThat(response.getData().getContents()).hasSize(1);
        }

        @DisplayName("마지막 페이지인경우 nextCursor = -1")
        @Test
        void 내가_작성한_가게_목록_조회시_요청한_개수보다_적은_가게를_반환하면_마지막_스크롤이라고_판단하고_마이너스_1을_반환한다() throws Exception {
            // given
            Store store1 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게1");
            Store store2 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게2");
            Store store3 = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게3");
            storeRepository.saveAll(List.of(store1, store2, store3));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, store2.getId());

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getTotalElements()).isEqualTo(3);
            assertThat(response.getData().getContents()).hasSize(1);
            assertStoreWithVisitsResponse(response.getData().getContents().get(0), store1);
        }

        @Test
        void 내가_작성한_가게_목록_조회시_삭제된_가게는_삭제된_가게로_조회된다() throws Exception {
            // given
            Store store = StoreCreator.createDeletedWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, null);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(1);
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getContents()).hasSize(1);
            assertStoreWithVisitsResponse(response.getData().getContents().get(0), store.getId(), store.getLatitude(), store.getLongitude(), store.getName(), store.getRating());

            assertThat(response.getData().getContents().get(0).getCategories()).hasSize(1);
            assertThat(response.getData().getContents().get(0).getCategories()).containsExactlyInAnyOrder(MenuCategoryType.BUNGEOPPANG);
        }

        @Deprecated
        @Test
        void V2버전에서는_내가_작성한_가게_목록_조회시_삭제된_가게는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.createDeletedWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            RetrieveMyStoresV2Request request = RetrieveMyStoresV2Request.testInstance(2, null, null, null, null);

            // when
            ApiResponse<StoresScrollV2Response> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistoriesV2(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(0);
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getContents()).isEmpty();
        }

        @Test
        void 내가_작성한_가게_목록_조회시_한달간의_방문_목록_카운트를_반환한다() throws Exception {
            // given
            Store store = StoreCreator.createWithDefaultMenu(testUser.getId(), "가게 이름");
            storeRepository.save(store);

            LocalDate today = LocalDate.now();
            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, today.minusMonths(1)),
                VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, today.minusWeeks(1)),
                VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, today))
            );

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(1, null);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.retrieveMyReportedStoreHistories(request, token, 200);

            // then
            assertThat(response.getData().getContents()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().getContents().get(0).getVisitHistory(), 1, 2, false);
        }

    }

    @Deprecated
    @DisplayName("GET /api/v2/stores/distance")
    @Nested
    class 가게_리스트_거리수_그룹화_조회 {

        @Test
        void 특정_카테고리의_가게_리스트를_거리수를_그룹화해서_보여준다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.DALGONA;

            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34.0001, 126);
            store1.addMenus(List.of(MenuCreator.create(store1, "메뉴2", "가격2", menuCategoryType)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게1", 34.001, 126);
            store2.addMenus(List.of(MenuCreator.create(store2, "메뉴2", "가격2", menuCategoryType)));

            storeRepository.saveAll(List.of(store1, store2));

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByDistance(request, 200);

            // then
            assertThat(response.getData().getStoreList50()).hasSize(1);
            assertThat(response.getData().getStoreList50().get(0).getStoreId()).isEqualTo(store1.getId());

            assertThat(response.getData().getStoreList100()).isEmpty();

            assertThat(response.getData().getStoreList500()).hasSize(1);
            assertThat(response.getData().getStoreList500().get(0).getStoreId()).isEqualTo(store2.getId());

            assertThat(response.getData().getStoreList1000()).isEmpty();
            assertThat(response.getData().getStoreListOver1000()).isEmpty();
        }

        @Test
        void 같은_그룹내에서_거리가_가까운_순서대로_조회된다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.GUKWAPPANG;

            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34.00015, 126);
            store1.addMenus(List.of(MenuCreator.create(store1, "메뉴2", "가격2", menuCategoryType)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게1", 34.0001, 126);
            store2.addMenus(List.of(MenuCreator.create(store2, "메뉴2", "가격2", menuCategoryType)));

            storeRepository.saveAll(List.of(store1, store2));

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByDistance(request, 200);

            // then
            assertThat(response.getData().getStoreList50()).hasSize(2);
            assertThat(response.getData().getStoreList50().get(0).getStoreId()).isEqualTo(store2.getId());
            assertThat(response.getData().getStoreList50().get(1).getStoreId()).isEqualTo(store1.getId());
        }

        @Test
        void 다른_카테고리는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게1", 34, 126);
            store.addMenus(List.of(
                MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.EOMUK),
                MenuCreator.create(store, "메뉴2", "가격2", MenuCategoryType.GUKWAPPANG),
                MenuCreator.create(store, "메뉴3", "가격3", MenuCategoryType.GYERANPPANG)
            ));
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByDistance(request, 200);

            // then
            assertThat(response.getData().getStoreList50()).isEmpty();
            assertThat(response.getData().getStoreList100()).isEmpty();
            assertThat(response.getData().getStoreList500()).isEmpty();
            assertThat(response.getData().getStoreList1000()).isEmpty();
            assertThat(response.getData().getStoreListOver1000()).isEmpty();
        }

        @Test
        void 삭제된_가게는_조회되지_않는다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.GYERANPPANG;

            Store store = StoreCreator.createDeleted(testUser.getId(), "가게 이름", 34.0, 126.0);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴1", "가격1", menuCategoryType)));
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByDistance(request, 200);

            // then
            assertThat(response.getData().getStoreList50()).isEmpty();
            assertThat(response.getData().getStoreList100()).isEmpty();
            assertThat(response.getData().getStoreList500()).isEmpty();
            assertThat(response.getData().getStoreList1000()).isEmpty();
            assertThat(response.getData().getStoreListOver1000()).isEmpty();
        }

        @Test
        void 거리순으로_내_주변의_특정_카테고리_가게_조회시_방문_정보도_함께_조회된다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.GYERANPPANG;

            Store store = StoreCreator.create(testUser.getId(), "가게 이름", 34.0, 126.0);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", menuCategoryType)));
            storeRepository.save(store);
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.now()));

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByDistance(request, 200);

            // then
            assertThat(response.getData().getStoreList50()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().getStoreList50().get(0).getVisitHistory(), 1, 0, true);
        }

    }

    @Deprecated
    @DisplayName("GET /api/v2/stores/review")
    @Nested
    class 가게_리스트_리뷰_평가_점수_그룹화_조회 {

        @Test
        void 특정_카테고리의_가게_리스트를_리뷰_평가_점수로_그룹화해서_보여준다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.GYERANPPANG;

            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 126, 1);
            store1.addMenus(List.of(MenuCreator.create(store1, "메뉴2", "가격2", menuCategoryType)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 126, 2);
            store2.addMenus(List.of(MenuCreator.create(store2, "메뉴2", "가격2", menuCategoryType)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 126, 3);
            store3.addMenus(List.of(MenuCreator.create(store3, "메뉴2", "가격2", menuCategoryType)));

            Store store4 = StoreCreator.create(testUser.getId(), "가게4", 34, 126, 4);
            store4.addMenus(List.of(MenuCreator.create(store4, "메뉴2", "가격2", menuCategoryType)));

            Store store5 = StoreCreator.create(testUser.getId(), "가게5", 34, 126, 5);
            store5.addMenus(List.of(MenuCreator.create(store5, "메뉴1", "가격1", menuCategoryType)));

            storeRepository.saveAll(List.of(store1, store2, store3, store4, store5));

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).hasSize(1);
            assertThat(response.getData().getStoreList1().get(0).getStoreId()).isEqualTo(store1.getId());

            assertThat(response.getData().getStoreList2()).hasSize(1);
            assertThat(response.getData().getStoreList2().get(0).getStoreId()).isEqualTo(store2.getId());

            assertThat(response.getData().getStoreList3()).hasSize(1);
            assertThat(response.getData().getStoreList3().get(0).getStoreId()).isEqualTo(store3.getId());

            assertThat(response.getData().getStoreList4()).hasSize(2);
            assertThat(response.getData().getStoreList4().get(0).getStoreId()).isEqualTo(store5.getId());
            assertThat(response.getData().getStoreList4().get(1).getStoreId()).isEqualTo(store4.getId());
        }

        @Test
        void 같은_그룹내에서_높은_리뷰부터_보여진다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.GYERANPPANG;

            Store store1 = StoreCreator.create(testUser.getId(), "가게 1.1", 34, 126, 1.1);
            store1.addMenus(List.of(MenuCreator.create(store1, "메뉴2", "가격2", menuCategoryType)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게 1.5", 34, 126, 1.5);
            store2.addMenus(List.of(MenuCreator.create(store2, "메뉴2", "가격2", menuCategoryType)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게 1.9", 34, 126, 1.9);
            store3.addMenus(List.of(MenuCreator.create(store3, "메뉴2", "가격2", menuCategoryType)));

            storeRepository.saveAll(List.of(store1, store2, store3));

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).hasSize(3);
            assertThat(response.getData().getStoreList1().get(0).getStoreId()).isEqualTo(store3.getId());
            assertThat(response.getData().getStoreList1().get(1).getStoreId()).isEqualTo(store2.getId());
            assertThat(response.getData().getStoreList1().get(2).getStoreId()).isEqualTo(store1.getId());
        }

        @Test
        void 다른_카테고리는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게1", 34, 126, 1.1);
            store.addMenus(List.of(
                MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.EOMUK),
                MenuCreator.create(store, "메뉴2", "가격2", MenuCategoryType.GUKWAPPANG),
                MenuCreator.create(store, "메뉴3", "가격3", MenuCategoryType.GYERANPPANG)
            ));
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).isEmpty();
            assertThat(response.getData().getStoreList2()).isEmpty();
            assertThat(response.getData().getStoreList3()).isEmpty();
            assertThat(response.getData().getStoreList4()).isEmpty();
        }

        @Test
        void 삭제된_가게는_조회되지_않는다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.BUNGEOPPANG;

            Store store = StoreCreator.createDeleted(testUser.getId(), "storeName");
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", menuCategoryType);
            store.addMenus(List.of(menu));
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).isEmpty();
            assertThat(response.getData().getStoreList2()).isEmpty();
            assertThat(response.getData().getStoreList3()).isEmpty();
            assertThat(response.getData().getStoreList4()).isEmpty();
        }

        @Test
        void 리뷰순으로_내_주변의_특정_카테고리_가게_조회시_방문_정보도_함께_조회된다() throws Exception {
            // given
            MenuCategoryType menuCategoryType = MenuCategoryType.BUNGEOPPANG;

            Store store = StoreCreator.create(testUser.getId(), "가게 1.1", 34, 126, 1.1);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴1", "가격1", menuCategoryType)));
            storeRepository.save(store);
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.now()));

            RetrieveStoreGroupByCategoryV2Request request = RetrieveStoreGroupByCategoryV2Request.testBuilder()
                .category(menuCategoryType)
                .latitude(34.0)
                .longitude(126.0)
                .mapLatitude(34.0)
                .mapLongitude(126.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewV2Response> response = storeRetrieveMockApiCaller.getStoresGroupByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).hasSize(1);
            assertVisitHistoryInfoResponse(response.getData().getStoreList1().get(0).getVisitHistory(), 0, 1, false);
        }

    }

}
