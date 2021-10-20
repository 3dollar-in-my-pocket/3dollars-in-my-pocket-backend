package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.service.review.dto.response.ReviewWithWriterResponse;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreGroupByCategoryRequest;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreDetailInfoRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.menu.Menu;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_STORE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreRetrieveControllerTest extends AbstractControllerTest {

    private StoreRetrieveMockApiCaller storeRetrieveMockApiCaller;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
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
    private StoreImageRepository storeImageRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        reviewRepository.deleteAll();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        visitHistoryRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        storeImageRepository.deleteAll();
    }

    @DisplayName("GET /api/v2/stores/near")
    @Nested
    class 사용자_주변의_가게_조회 {

        @Test
        void 사용자_주변의_가게들을_조회한다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            Menu menu1 = MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            Menu menu2 = MenuCreator.create(store1, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG);
            Menu menu3 = MenuCreator.create(store1, "메뉴3", "가격3", MenuCategoryType.EOMUK);
            store1.addMenus(Arrays.asList(menu1, menu2, menu3));

            Store store2 = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.saveAll(Arrays.asList(store1, store2));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testInstance(34, 124, 34, 124, 1000);

            // when
            ApiResponse<List<StoreInfoResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertStoreInfoResponse(response.getData().get(0), store1.getId(), store1.getLatitude(), store1.getLongitude(), store1.getName(), store1.getRating());
            assertThat(response.getData().get(0).getCategories()).hasSize(2);
            assertThat(response.getData().get(0).getCategories().get(0)).isEqualTo(MenuCategoryType.BUNGEOPPANG);
            assertThat(response.getData().get(0).getCategories().get(1)).isEqualTo(MenuCategoryType.EOMUK);

            assertStoreInfoResponse(response.getData().get(1), store2.getId(), store2.getLatitude(), store2.getLongitude(), store2.getName(), store2.getRating());
            assertThat(response.getData().get(1).getCategories()).hasSize(1);
            assertThat(response.getData().get(1).getCategories().get(0)).isEqualTo(MenuCategoryType.BUNGEOPPANG);
        }

        @Test
        void 사용자_주위의_가게를_조회시_삭제된_가게는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            store.delete();
            storeRepository.save(store);

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testInstance(34, 124, 34, 124, 1000);

            // when
            ApiResponse<List<StoreInfoResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).isEmpty();
        }

        @Test
        void 사용자_주위의_가게를_조회할때_방문_인증_횟수가_함께_조회된다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store1.addMenus(List.of(MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store2.addMenus(List.of(MenuCreator.create(store2, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.saveAll(Arrays.asList(store1, store2));

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store1, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 18)),
                VisitHistoryCreator.create(store2, 100L, VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 18))
            ));

            RetrieveNearStoresRequest request = RetrieveNearStoresRequest.testInstance(34, 124, 34, 124, 1000);

            // when
            ApiResponse<List<StoreInfoResponse>> response = storeRetrieveMockApiCaller.getNearStores(request, 200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertThat(response.getData().get(0).getVisitHistory().getExistsVisitsCount()).isEqualTo(1);
            assertThat(response.getData().get(0).getVisitHistory().getNotExistsVisitsCount()).isEqualTo(0);
            assertThat(response.getData().get(1).getVisitHistory().getExistsVisitsCount()).isEqualTo(0);
            assertThat(response.getData().get(1).getVisitHistory().getNotExistsVisitsCount()).isEqualTo(1);
        }

    }

    @DisplayName("GET /api/v2/store")
    @Nested
    class 특정_가게_상세_정보_조회 {

        @Test
        void 특정_가게에_대한_상세_정보를_조회한다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);

            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(Collections.singletonList(menu));

            DayOfTheWeek day = DayOfTheWeek.FRIDAY;
            store.addAppearanceDays(Collections.singleton(day));

            PaymentMethodType paymentMethodType = PaymentMethodType.CASH;
            store.addPaymentMethods(Collections.singleton(paymentMethodType));

            storeRepository.save(store);

            RetrieveStoreDetailInfoRequest request = RetrieveStoreDetailInfoRequest.testInstance(store.getId(), 34.0, 124.0);

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertStoreDetailInfoResponse(data, store.getId(), store.getLatitude(), store.getLongitude(), store.getName(), store.getType(), store.getRating());

            assertThat(data.getCategories()).hasSize(1);
            assertThat(data.getCategories().get(0)).isEqualTo(menu.getCategory());

            assertThat(data.getMenus()).hasSize(1);
            assertMenuResponse(data.getMenus().get(0), menu.getId(), menu.getCategory(), menu.getName(), menu.getPrice());

            assertUserInfoResponse(data.getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());

            assertThat(data.getAppearanceDays()).hasSize(1);
            assertThat(data.getAppearanceDays().contains(day)).isTrue();

            assertThat(data.getPaymentMethods()).hasSize(1);
            assertThat(data.getPaymentMethods().contains(paymentMethodType)).isTrue();
        }

        @Test
        void 회원탈퇴한_유저의경우_사라진_제보자라고_표기된다() throws Exception {
            // given
            Store store = StoreCreator.create(999L, "storeName", 34, 124);
            store.addMenus(Collections.singletonList(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            RetrieveStoreDetailInfoRequest request = RetrieveStoreDetailInfoRequest.testInstance(store.getId(), 34.0, 124.0);

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertUserInfoResponse(data.getUser(), null, "사라진 제보자", null);
        }

        @Test
        void 가게에_작성된_리뷰_와_작성자_정보가_최근_생성된_순서로_조회되고_회원탈퇴한_유저는_사라진_제보자로_표기된다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store.addMenus(Collections.singletonList(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "댓글 1", 5);
            Review review2 = ReviewCreator.create(store.getId(), 999L, "댓글 2", 3);

            reviewRepository.saveAll(Arrays.asList(review1, review2));

            RetrieveStoreDetailInfoRequest request = RetrieveStoreDetailInfoRequest.testInstance(store.getId(), 34.0, 124.0);

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 200);

            // then
            StoreDetailResponse data = response.getData();
            assertThat(data.getReviews()).hasSize(2);
            assertReviewWithWriterResponse(data.getReviews().get(0), review2);
            assertUserInfoResponse(data.getReviews().get(0).getUser(), null, "사라진 제보자", null);

            assertReviewWithWriterResponse(data.getReviews().get(1), review1);
            assertUserInfoResponse(data.getReviews().get(1).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());
        }

        @Test
        void 존재하지_않는_가게인경우_NOTFOUND_404_NOT_FOUND() throws Exception {
            // when
            RetrieveStoreDetailInfoRequest request = RetrieveStoreDetailInfoRequest.testInstance(999L, 34.0, 124.0);

            // when
            ApiResponse<StoreDetailResponse> response = storeRetrieveMockApiCaller.getStoreDetailInfo(request, 404);

            // then
            assertThat(response.getResultCode()).isEqualTo(NOT_FOUND_STORE_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(NOT_FOUND_STORE_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

    }

    @DisplayName("GET /api/v2/store/storeId/images")
    @Nested
    class 특정_가게에_등록된_이미지들을_조회한다 {

        @Test
        void 가게에_등록된_사진들을_조회한다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store.addMenus(Collections.singletonList(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            StoreImage storeImage1 = StoreImage.newInstance(store.getId(), testUser.getId(), "image1");
            StoreImage storeImage2 = StoreImage.newInstance(store.getId(), testUser.getId(), "image1");

            storeImageRepository.saveAll(List.of(storeImage1, storeImage2));

            // when
            ApiResponse<List<StoreImageResponse>> response = storeRetrieveMockApiCaller.retrieveStoreImages(store.getId(), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertStoreImageResponse(response.getData().get(0), storeImage1.getId(), storeImage1.getUrl()),
                () -> assertStoreImageResponse(response.getData().get(1), storeImage2.getId(), storeImage2.getUrl()));
        }

    }

    private void assertStoreImageResponse(StoreImageResponse response, Long storeImageId, String url) {
        assertThat(response.getImageId()).isEqualTo(storeImageId);
        assertThat(response.getUrl()).isEqualTo(url);
    }

    @DisplayName("GET /api/v2/stores/me")
    @Nested
    class 사용자가_작성한_가게들_조회 {

        @Test
        void 첫_페이지_조회시_다음_커서가_반환된다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 124);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 124);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴3", "가격3", MenuCategoryType.BUNGEOPPANG)));

            Store store4 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store4.addMenus(Collections.singletonList(MenuCreator.create(store4, "메뉴4", "가격4", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3, store4));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, null, null, 34, 124);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(4);
            assertThat(response.getData().getNextCursor()).isEqualTo(store3.getId());
            assertThat(response.getData().getContents()).hasSize(2);
            assertStoreInfoResponse(response.getData().getContents().get(0), store4);
            assertStoreInfoResponse(response.getData().getContents().get(1), store3);
        }

        @Test
        void 중간_페이지_조회시_다음_커서가_반환된다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 124);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 124);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴3", "가격3", MenuCategoryType.BUNGEOPPANG)));

            Store store4 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store4.addMenus(Collections.singletonList(MenuCreator.create(store4, "메뉴4", "가격4", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3, store4));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, store4.getId(), 4L, 34, 124);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(4);
            assertThat(response.getData().getNextCursor()).isEqualTo(store2.getId());
            assertThat(response.getData().getContents()).hasSize(2);
            assertStoreInfoResponse(response.getData().getContents().get(0), store3);
            assertStoreInfoResponse(response.getData().getContents().get(1), store2);
        }

        @Test
        void 중간_페이지_조회시_다음_커서가_반환된다_총개수가_캐싱되지_않으면_계산되서_반환() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 124);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 124);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴3", "가격3", MenuCategoryType.BUNGEOPPANG)));

            Store store4 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store4.addMenus(Collections.singletonList(MenuCreator.create(store4, "메뉴4", "가격4", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3, store4));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, store4.getId(), null, 33, 123);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(4);
            assertThat(response.getData().getNextCursor()).isEqualTo(store2.getId());
            assertThat(response.getData().getContents()).hasSize(2);
            assertStoreInfoResponse(response.getData().getContents().get(0), store3);
            assertStoreInfoResponse(response.getData().getContents().get(1), store2);
        }

        @DisplayName("마지막 페이지인경우 nextCursor = -1")
        @Test
        void 다음_커서의_가게를_한개_추가_조회시_조회되지_않으면_마지막_커서로_판단한다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 124);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 124);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴3", "가격3", MenuCategoryType.BUNGEOPPANG)));

            Store store4 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store4.addMenus(Collections.singletonList(MenuCreator.create(store4, "메뉴4", "가격4", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3, store4));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, store3.getId(), null, 34, 124);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(4);
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getContents()).hasSize(2);
            assertStoreInfoResponse(response.getData().getContents().get(0), store2);
            assertStoreInfoResponse(response.getData().getContents().get(1), store1);
        }

        @DisplayName("마지막 페이지인경우 nextCursor = -1")
        @Test
        void 조회한_size_보다_적은_가게가_조회되면_마지막_커서로_판단한다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 124);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 124);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 124);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴3", "가격3", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, store2.getId(), 3L, 34, 124);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(3);
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getContents()).hasSize(1);
            assertStoreInfoResponse(response.getData().getContents().get(0), store1);
        }

        @Test
        void 삭제된_가게는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            store.delete();
            storeRepository.save(store);

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, null, null, 34, 124);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(0);
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getContents()).isEmpty();
        }

        @Test
        void 사용자가_제보한_가게들_조회시_방문_횟수도_함께_조회된다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            storeRepository.save(store);

            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 18)));

            RetrieveMyStoresRequest request = RetrieveMyStoresRequest.testInstance(2, null, null, 34, 124);

            // when
            ApiResponse<StoresScrollResponse> response = storeRetrieveMockApiCaller.getMyStores(request, token, 200);

            // then
            assertThat(response.getData().getTotalElements()).isEqualTo(1);
            assertThat(response.getData().getNextCursor()).isEqualTo(-1);
            assertThat(response.getData().getContents().get(0).getVisitHistory().getExistsVisitsCount()).isEqualTo(0);
            assertThat(response.getData().getContents().get(0).getVisitHistory().getNotExistsVisitsCount()).isEqualTo(1);
        }

    }

    @DisplayName("GET /api/v2/stores/distance")
    @Nested
    class 가게_리스트_거리수_그룹화_조회 {

        @Test
        void 특정_카테고리의_가게_리스트를_리뷰_평가_점수로_그룹화해서_보여준다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34.0001, 124, 1);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게1", 34.001, 124, 1);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2));

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceResponse> response = storeRetrieveMockApiCaller.getStoresByDistance(request, 200);

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
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34.00015, 124, 1);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게1", 34.0001, 124, 1);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2));

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceResponse> response = storeRetrieveMockApiCaller.getStoresByDistance(request, 200);

            assertThat(response.getData().getStoreList50()).hasSize(2);
            assertThat(response.getData().getStoreList50().get(0).getStoreId()).isEqualTo(store2.getId());
            assertThat(response.getData().getStoreList50().get(1).getStoreId()).isEqualTo(store1.getId());
        }

        @Test
        void 다른_카테고리는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게1", 34, 124, 1.1);
            store.addMenus(Arrays.asList(
                MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.EOMUK),
                MenuCreator.create(store, "메뉴2", "가격2", MenuCategoryType.GUKWAPPANG),
                MenuCreator.create(store, "메뉴3", "가격3", MenuCategoryType.GYERANPPANG)
            ));
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceResponse> response = storeRetrieveMockApiCaller.getStoresByDistance(request, 200);

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
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            store.delete();
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceResponse> response = storeRetrieveMockApiCaller.getStoresByDistance(request, 200);

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
            Store store = StoreCreator.create(testUser.getId(), "가게 1.1", 34, 124, 1.1);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            storeRepository.save(store);
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.EXISTS, LocalDate.of(2021, 10, 18)));

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByDistanceResponse> response = storeRetrieveMockApiCaller.getStoresByDistance(request, 200);

            assertThat(response.getData().getStoreList50()).hasSize(1);
            assertThat(response.getData().getStoreList50().get(0).getVisitHistory().getExistsVisitsCount()).isEqualTo(1);
            assertThat(response.getData().getStoreList50().get(0).getVisitHistory().getNotExistsVisitsCount()).isEqualTo(0);
        }

    }

    @DisplayName("GET /api/v2/stores/review")
    @Nested
    class 가게_리스트_리뷰_평가_점수_그룹화_조회 {

        @Test
        void 특정_카테고리의_가게_리스트를_리뷰_평가_점수로_그룹화해서_보여준다() throws Exception {
            // given
            Store store1 = StoreCreator.create(testUser.getId(), "가게1", 34, 124, 1);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게2", 34, 124, 2);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게3", 34, 124, 3);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store4 = StoreCreator.create(testUser.getId(), "가게4", 34, 124, 4);
            store4.addMenus(Collections.singletonList(MenuCreator.create(store4, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store5 = StoreCreator.create(testUser.getId(), "가게5", 34, 124, 5);
            store5.addMenus(Collections.singletonList(MenuCreator.create(store5, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3, store4, store5));

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewResponse> response = storeRetrieveMockApiCaller.getStoresByReview(request, 200);

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
            Store store1 = StoreCreator.create(testUser.getId(), "가게 1.1", 34, 124, 1.1);
            store1.addMenus(Collections.singletonList(MenuCreator.create(store1, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store2 = StoreCreator.create(testUser.getId(), "가게 1.5", 34, 124, 1.5);
            store2.addMenus(Collections.singletonList(MenuCreator.create(store2, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            Store store3 = StoreCreator.create(testUser.getId(), "가게 1.9", 34, 124, 1.9);
            store3.addMenus(Collections.singletonList(MenuCreator.create(store3, "메뉴2", "가격2", MenuCategoryType.BUNGEOPPANG)));

            storeRepository.saveAll(Arrays.asList(store1, store2, store3));

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewResponse> response = storeRetrieveMockApiCaller.getStoresByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).hasSize(3);
            assertThat(response.getData().getStoreList1().get(0).getStoreId()).isEqualTo(store3.getId());
            assertThat(response.getData().getStoreList1().get(1).getStoreId()).isEqualTo(store2.getId());
            assertThat(response.getData().getStoreList1().get(2).getStoreId()).isEqualTo(store1.getId());
        }

        @Test
        void 다른_카테고리는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게1", 34, 124, 1.1);
            store.addMenus(Arrays.asList(
                MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.EOMUK),
                MenuCreator.create(store, "메뉴2", "가격2", MenuCategoryType.GUKWAPPANG),
                MenuCreator.create(store, "메뉴3", "가격3", MenuCategoryType.GYERANPPANG)
            ));
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewResponse> response = storeRetrieveMockApiCaller.getStoresByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).isEmpty();
            assertThat(response.getData().getStoreList2()).isEmpty();
            assertThat(response.getData().getStoreList3()).isEmpty();
            assertThat(response.getData().getStoreList4()).isEmpty();
        }

        @Test
        void 삭제된_가게는_조회되지_않는다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            store.delete();
            storeRepository.save(store);

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewResponse> response = storeRetrieveMockApiCaller.getStoresByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).isEmpty();
            assertThat(response.getData().getStoreList2()).isEmpty();
            assertThat(response.getData().getStoreList3()).isEmpty();
            assertThat(response.getData().getStoreList4()).isEmpty();
        }

        @Test
        void 리뷰순으로_내_주변의_특정_카테고리_가게_조회시_방문_정보도_함께_조회된다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "가게 1.1", 34, 124, 1.1);
            Menu menu = MenuCreator.create(store, "메뉴1", "가격1", MenuCategoryType.BUNGEOPPANG);
            store.addMenus(List.of(menu));
            storeRepository.save(store);
            visitHistoryRepository.save(VisitHistoryCreator.create(store, testUser.getId(), VisitType.NOT_EXISTS, LocalDate.of(2021, 10, 18)));

            RetrieveStoreGroupByCategoryRequest request = RetrieveStoreGroupByCategoryRequest.testBuilder()
                .category(MenuCategoryType.BUNGEOPPANG)
                .latitude(34.0)
                .longitude(124.0)
                .mapLatitude(34.0)
                .mapLongitude(124.0)
                .build();

            // when
            ApiResponse<StoresGroupByReviewResponse> response = storeRetrieveMockApiCaller.getStoresByReview(request, 200);

            // then
            assertThat(response.getData().getStoreList1()).hasSize(1);
            assertThat(response.getData().getStoreList1().get(0).getVisitHistory().getExistsVisitsCount()).isEqualTo(0);
            assertThat(response.getData().getStoreList1().get(0).getVisitHistory().getNotExistsVisitsCount()).isEqualTo(1);
        }
    }

    private void assertReviewWithWriterResponse(ReviewWithWriterResponse response, Review review) {
        assertThat(response.getReviewId()).isEqualTo(review.getId());
        assertThat(response.getContents()).isEqualTo(review.getContents());
        assertThat(response.getRating()).isEqualTo(review.getRating());
    }

    private void assertStoreInfoResponse(StoreInfoResponse response, Store store) {
        assertThat(response.getStoreId()).isEqualTo(store.getId());
        assertThat(response.getStoreName()).isEqualTo(store.getName());
        assertThat(response.getLatitude()).isEqualTo(store.getLatitude());
        assertThat(response.getLongitude()).isEqualTo(store.getLongitude());
        assertThat(response.getRating()).isEqualTo(store.getRating());
    }

    private void assertUserInfoResponse(UserInfoResponse user, Long userId, String name, UserSocialType socialType) {
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getSocialType()).isEqualTo(socialType);
    }

    private void assertMenuResponse(MenuResponse respose, Long menuId, MenuCategoryType category, String name, String price) {
        assertThat(respose.getMenuId()).isEqualTo(menuId);
        assertThat(respose.getCategory()).isEqualTo(category);
        assertThat(respose.getName()).isEqualTo(name);
        assertThat(respose.getPrice()).isEqualTo(price);
    }

    private void assertStoreDetailInfoResponse(StoreDetailResponse response, Long storeId, Double latitude, Double longitude, String name, StoreType type, double rating) {
        assertThat(response.getStoreId()).isEqualTo(storeId);
        assertThat(response.getLatitude()).isEqualTo(latitude);
        assertThat(response.getLongitude()).isEqualTo(longitude);
        assertThat(response.getStoreName()).isEqualTo(name);
        assertThat(response.getStoreType()).isEqualTo(type);
        assertThat(response.getRating()).isEqualTo(rating);
    }

    private void assertStoreInfoResponse(StoreInfoResponse response, Long id, Double latitude, Double longitude, String name, double rating) {
        assertThat(response.getStoreId()).isEqualTo(id);
        assertThat(response.getLatitude()).isEqualTo(latitude);
        assertThat(response.getLongitude()).isEqualTo(longitude);
        assertThat(response.getStoreName()).isEqualTo(name);
        assertThat(response.getRating()).isEqualTo(rating);
    }

}
