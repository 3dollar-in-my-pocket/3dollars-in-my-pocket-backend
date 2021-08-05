package com.depromeet.threedollar.api.controller.review;

import com.depromeet.threedollar.api.common.dto.ApiResponse;
import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewDetailResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewScrollResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.review.ReviewStatus;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewControllerTest extends AbstractControllerTest {

    private ReviewMockApiCaller reviewMockApiCaller;

    private Store store;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        reviewMockApiCaller = new ReviewMockApiCaller(mockMvc, objectMapper);

        store = StoreCreator.create(testUser.getId(), "디프만 붕어빵");
        store.addMenus(Collections.singletonList(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
        storeRepository.save(store);
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        reviewRepository.deleteAll();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @DisplayName("POST /api/v2/store/review 200 OK")
    @Test
    void 가게에_새로운_리뷰를_등록한다() throws Exception {
        // given
        AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), "content", 5);

        // when
        ApiResponse<ReviewInfoResponse> response = reviewMockApiCaller.addStoreReview(request, token, 200);

        // then
        assertReviewInfoResponse(response.getData(), store.getId(), request.getContents(), request.getRating(), ReviewStatus.POSTED);
    }

    @DisplayName("PUT /api/v2/store/review 200 OK")
    @Test
    void 사용자가_작성한_리뷰를_수정한다() throws Exception {
        // given
        Review review = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요", 5);
        reviewRepository.save(review);

        UpdateReviewRequest request = UpdateReviewRequest.testInstance("맛이 없어졌어요", 1);

        // when
        ApiResponse<ReviewInfoResponse> response = reviewMockApiCaller.updateStoreReview(review.getId(), request, token, 200);

        // then
        assertReviewInfoResponse(response.getData(), store.getId(), request.getContents(), request.getRating(), ReviewStatus.POSTED);
    }

    @DisplayName("DELETE /api/v2/store/review 200 OK")
    @Test
    void 사용자가_작성한_리뷰를_삭제한다() throws Exception {
        // given
        Review review = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요", 5);
        reviewRepository.save(review);

        // when
        ApiResponse<String> response = reviewMockApiCaller.deleteStoreReview(review.getId(), token, 200);

        // then
        assertThat(response.getData()).isEqualTo("OK");
    }

    @DisplayName("GET /api/v2/store/reviews/me 200 OK - 첫페이지")
    @Test
    void 사용자가_작성한_리뷰를_조회한다_첫번째_스크롤() throws Exception {
        // given
        Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요1", 5);
        Review review2 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요2", 4);
        Review review3 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요3", 3);
        Review review4 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요4", 2);
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

        RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testInstance(2, null, null);

        // when
        ApiResponse<ReviewScrollResponse> response = reviewMockApiCaller.retrieveMyStoreReviews(request, token, 200);

        // then
        assertThat(response.getData().getTotalElements()).isEqualTo(4);
        assertThat(response.getData().getNextCursor()).isEqualTo(review3.getId());
        assertThat(response.getData().getContents()).hasSize(2);

        assertReviewInfoResponse(response.getData().getContents().get(0), review4.getId(), store.getId(), store.getName(), review4.getContents(), review4.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(0).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());

        assertReviewInfoResponse(response.getData().getContents().get(1), review3.getId(), store.getId(), store.getName(), review3.getContents(), review3.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(1).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());
    }

    @DisplayName("GET /api/v2/store/reviews/me 200 OK - 중간 페이지")
    @Test
    void 사용자가_작성한_리뷰를_조회한다_중간_스크롤() throws Exception {
        // given
        Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요1", 5);
        Review review2 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요2", 4);
        Review review3 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요3", 3);
        Review review4 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요4", 2);
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

        RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testInstance(2, review4.getId(), 4L);

        // when
        ApiResponse<ReviewScrollResponse> response = reviewMockApiCaller.retrieveMyStoreReviews(request, token, 200);

        // then
        assertThat(response.getData().getTotalElements()).isEqualTo(4);
        assertThat(response.getData().getNextCursor()).isEqualTo(review2.getId());
        assertThat(response.getData().getContents()).hasSize(2);

        assertReviewInfoResponse(response.getData().getContents().get(0), review3.getId(), store.getId(), store.getName(), review3.getContents(), review3.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(0).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());

        assertReviewInfoResponse(response.getData().getContents().get(1), review2.getId(), store.getId(), store.getName(), review2.getContents(), review2.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(1).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());
    }

    @DisplayName("GET /api/v2/store/reviews/me 200 OK - 중간 페이지 (캐시 X)")
    @Test
    void 사용자가_작성한_리뷰를_조회시_전체_리뷰수가_반환된다() throws Exception {
        // given
        Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요1", 5);
        Review review2 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요2", 4);
        Review review3 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요3", 3);
        Review review4 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요4", 2);
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

        RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testInstance(2, review4.getId(), null);

        // when
        ApiResponse<ReviewScrollResponse> response = reviewMockApiCaller.retrieveMyStoreReviews(request, token, 200);

        // then
        assertThat(response.getData().getTotalElements()).isEqualTo(4);
        assertThat(response.getData().getNextCursor()).isEqualTo(review2.getId());
        assertThat(response.getData().getContents()).hasSize(2);

        assertReviewInfoResponse(response.getData().getContents().get(0), review3.getId(), store.getId(), store.getName(), review3.getContents(), review3.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(0).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());

        assertReviewInfoResponse(response.getData().getContents().get(1), review2.getId(), store.getId(), store.getName(), review2.getContents(), review2.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(1).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());
    }

    @DisplayName("GET /api/v2/store/reviews/me 200 OK - 마지막 페이지")
    @Test
    void 사용자가_작성한_리뷰를_조회한다_마지막_스크롤1() throws Exception {
        // given
        Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요1", 5);
        Review review2 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요2", 4);
        Review review3 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요3", 3);
        Review review4 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요4", 2);
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

        RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testInstance(2, review3.getId(), null);

        // when
        ApiResponse<ReviewScrollResponse> response = reviewMockApiCaller.retrieveMyStoreReviews(request, token, 200);

        // then
        assertThat(response.getData().getTotalElements()).isEqualTo(4);
        assertThat(response.getData().getNextCursor()).isNull();
        assertThat(response.getData().getContents()).hasSize(2);

        assertReviewInfoResponse(response.getData().getContents().get(0), review2.getId(), store.getId(), store.getName(), review2.getContents(), review2.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(0).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());

        assertReviewInfoResponse(response.getData().getContents().get(1), review1.getId(), store.getId(), store.getName(), review1.getContents(), review1.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(1).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());
    }

    @DisplayName("GET /api/v2/store/reviews/me 200 OK - 마지막 페이지")
    @Test
    void 사용자가_작성한_리뷰를_조회한다_마지막_스크롤2() throws Exception {
        // given
        Review review1 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요1", 5);
        Review review2 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요2", 4);
        Review review3 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요3", 3);
        Review review4 = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요4", 2);
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

        RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testInstance(2, review2.getId(), null);

        // when
        ApiResponse<ReviewScrollResponse> response = reviewMockApiCaller.retrieveMyStoreReviews(request, token, 200);

        // then
        assertThat(response.getData().getTotalElements()).isEqualTo(4);
        assertThat(response.getData().getNextCursor()).isNull();
        assertThat(response.getData().getContents()).hasSize(1);

        assertReviewInfoResponse(response.getData().getContents().get(0), review1.getId(), store.getId(), store.getName(), review1.getContents(), review1.getRating(), ReviewStatus.POSTED);
        assertUserInfoResponse(response.getData().getContents().get(0).getUser(), testUser.getId(), testUser.getName(), testUser.getSocialType());
    }

    @DisplayName("GET /api/v2/store/reviews/me 200 Ok - 삭제된 리뷰는 조회되지 않음")
    @Test
    void 사용자가_작성한_리뷰를_조회시_삭제된_리뷰는_조회되지_않는다() throws Exception {
        // given
        Review review = ReviewCreator.create(store.getId(), testUser.getId(), "너무 맛있어요", 5);
        review.delete();
        reviewRepository.save(review);

        RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testInstance(2, null, null);

        // when
        ApiResponse<ReviewScrollResponse> response = reviewMockApiCaller.retrieveMyStoreReviews(request, token, 200);

        // then
        assertThat(response.getData().getTotalElements()).isEqualTo(0);
        assertThat(response.getData().getNextCursor()).isNull();
        assertThat(response.getData().getContents()).isEmpty();
    }

    private void assertUserInfoResponse(UserInfoResponse user, Long id, String name, UserSocialType socialType) {
        assertThat(user.getUserId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getSocialType()).isEqualTo(socialType);
    }

    private void assertReviewInfoResponse(ReviewInfoResponse response, Long storeId, String contents, int rating, ReviewStatus status) {
        assertThat(response.getStoreId()).isEqualTo(storeId);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getRating()).isEqualTo(rating);
        assertThat(response.getStatus()).isEqualTo(status);
    }

    private void assertReviewInfoResponse(ReviewDetailResponse response, Long reviewId, Long storeId, String storeName, String contents, int rating, ReviewStatus status) {
        assertThat(response.getStoreId()).isEqualTo(storeId);
        assertThat(response.getStoreName()).isEqualTo(storeName);
        assertThat(response.getContents()).isEqualTo(contents);
        assertThat(response.getRating()).isEqualTo(rating);
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getReviewId()).isEqualTo(reviewId);
    }

}
