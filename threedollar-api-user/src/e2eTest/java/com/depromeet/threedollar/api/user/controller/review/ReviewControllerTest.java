package com.depromeet.threedollar.api.user.controller.review;

import static com.depromeet.threedollar.api.user.controller.review.support.ReviewAssertions.assertReviewDetailInfoResponse;
import static com.depromeet.threedollar.api.user.controller.review.support.ReviewAssertions.assertReviewInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.user.controller.SetupStoreControllerTest;
import com.depromeet.threedollar.api.user.listener.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.user.listener.store.StoreRatingEventListener;
import com.depromeet.threedollar.api.user.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.user.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.user.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewStatus;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.user.event.review.ReviewChangedEvent;
import com.depromeet.threedollar.domain.rds.user.event.review.ReviewCreatedEvent;

class ReviewControllerTest extends SetupStoreControllerTest {

    private ReviewMockApiCaller reviewMockApiCaller;

    @Autowired
    private ReviewRepository reviewRepository;

    @MockBean
    private AddUserMedalEventListener addUserMedalEventListener;

    @MockBean
    private StoreRatingEventListener storeRatingEventListener;

    @BeforeEach
    void setUp() {
        reviewMockApiCaller = new ReviewMockApiCaller(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        reviewRepository.deleteAllInBatch();
    }

    @DisplayName("POST /api/v2/store/review")
    @Nested
    class AddNewReviewApiTest {

        @Test
        void 리뷰_등록_성공시_리뷰_정보가_반환된다() throws Exception {
            // given
            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(store.getId())
                .contents("붕어빵이 너무 맛있어요")
                .rating(5)
                .build();

            // when
            ApiResponse<ReviewInfoResponse> response = reviewMockApiCaller.addReview(request, token, 200);

            // then
            assertReviewInfoResponse(response.getData(), store.getId(), request.getContents(), request.getRating());
        }

        @Test
        void 리뷰_등록시_메달을_획득하는_작업이_수행된다() throws Exception {
            // given
            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(store.getId())
                .contents("우와")
                .rating(4)
                .build();

            // when
            reviewMockApiCaller.addReview(request, token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByAddReview(any(ReviewCreatedEvent.class));
        }

    }

    @DisplayName("PUT /api/v2/store/review")
    @Nested
    class UpdateReviewApiTest {

        @Test
        void 성공시_변경된_리뷰정보가_반환된다() throws Exception {
            // given
            Review review = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요", 5);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents("맛이 없어졌어요")
                .rating(1)
                .build();

            // when
            ApiResponse<ReviewInfoResponse> response = reviewMockApiCaller.updateReview(review.getId(), request, token, 200);

            // then
            assertReviewInfoResponse(response.getData(), store.getId(), request.getContents(), request.getRating());
        }

        @Test
        void 리뷰를_수정하면_가게의_평균_리뷰점수가_갱신된다() throws Exception {
            // given
            Review review = ReviewCreator.create(store.getId(), user.getId(), "맛 없어요", 2);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents("우와")
                .rating(4)
                .build();

            // when
            reviewMockApiCaller.updateReview(review.getId(), request, token, 200);

            // then
            verify(storeRatingEventListener, times(1)).renewStoreRating(any(ReviewChangedEvent.class));
        }

    }

    @DisplayName("DELETE /api/v2/store/review")
    @Nested
    class DeleteReviewApiTest {

        @Test
        void 자신이_작성한_리뷰를_삭제요청시_성공하면_200OK() throws Exception {
            // given
            Review review = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요", 5);
            reviewRepository.save(review);

            // when
            ApiResponse<String> response = reviewMockApiCaller.deleteReview(review.getId(), token, 200);

            // then
            assertThat(response.getData()).isEqualTo("OK");
        }

        @Test
        void 리뷰를_삭제하면_가게의_평균_리뷰점수가_갱신된다() throws Exception {
            // given
            Review review = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요", 5);
            reviewRepository.save(review);

            // when
            reviewMockApiCaller.deleteReview(review.getId(), token, 200);

            // then
            verify(storeRatingEventListener, times(1)).renewStoreRating(any(ReviewChangedEvent.class));
        }

    }

    @DisplayName("GET /api/v2/store/reviews/me")
    @Nested
    class GetMyReviewsApiTest {

        @Test
        void 사용자가_작성한_리뷰_첫_스크롤을_조회한다() throws Exception {
            // given
            Review review1 = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요1", 5);
            Review review2 = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요2", 4);
            Review review3 = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요3", 3);
            Review review4 = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요4", 2);
            reviewRepository.saveAll(List.of(review1, review2, review3, review4));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(review3.getId()),
                () -> assertThat(response.getData().isHasNext()).isTrue(),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(0), review4, store, user),
                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(1), review3, store, user)
            );
        }

        @Test
        void 사용자가_작성한_리뷰_중간_스크롤_조회시() throws Exception {
            // given
            Review review1 = ReviewCreator.create(store.getId(), user.getId(), "최고에요", 5);
            Review review2 = ReviewCreator.create(store.getId(), user.getId(), "맛있어요", 4);
            Review review3 = ReviewCreator.create(store.getId(), user.getId(), "그냥 그래요", 3);
            Review review4 = ReviewCreator.create(store.getId(), user.getId(), "좀 그래요", 2);
            reviewRepository.saveAll(List.of(review1, review2, review3, review4));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(review4.getId())
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(review2.getId()),
                () -> assertThat(response.getData().isHasNext()).isTrue(),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(0), review3, store, user),
                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(1), review2, store, user)
            );
        }

        @Test
        void 사용자가_작성한_리뷰_조회시_전체_리뷰수가_반환된다() throws Exception {
            // given
            Review review1 = ReviewCreator.create(store.getId(), user.getId(), "최고에요", 5);
            Review review2 = ReviewCreator.create(store.getId(), user.getId(), "맛있어요", 4);
            Review review3 = ReviewCreator.create(store.getId(), user.getId(), "그냥 그래요", 3);
            Review review4 = ReviewCreator.create(store.getId(), user.getId(), "좀 그래요", 2);
            reviewRepository.saveAll(List.of(review1, review2, review3, review4));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(review4.getId())
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(review2.getId()),
                () -> assertThat(response.getData().isHasNext()).isTrue(),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(0), review3, store, user),
                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(1), review2, store, user)
            );
        }

        @Test
        void 다음_커서의_리뷰를_한개_추가_조회시_조회되지_않으면_마지막_커서로_판단한다() throws Exception {
            // given
            Review review1 = ReviewCreator.create(store.getId(), user.getId(), "정말 맛있어요", 5);
            Review review2 = ReviewCreator.create(store.getId(), user.getId(), "그냥 맛있어요", 4);
            Review review3 = ReviewCreator.create(store.getId(), user.getId(), "좀 맛있어요", 3);
            Review review4 = ReviewCreator.create(store.getId(), user.getId(), "그냥 그래요", 2);
            reviewRepository.saveAll(List.of(review1, review2, review3, review4));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(review3.getId())
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(2),

                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(0), review2, store, user),
                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(1), review1, store, user)
            );
        }

        @Test
        void 조회한_size_보다_적은_리뷰가_조회되면_마지막_커서로_판단한다() throws Exception {
            // given
            Review review1 = ReviewCreator.create(store.getId(), user.getId(), "정말! 맛있어요", 5);
            Review review2 = ReviewCreator.create(store.getId(), user.getId(), "그냥! 맛있어요", 4);
            Review review3 = ReviewCreator.create(store.getId(), user.getId(), "좀! 맛있어요", 3);
            Review review4 = ReviewCreator.create(store.getId(), user.getId(), "그냥! 그래요", 2);
            reviewRepository.saveAll(List.of(review1, review2, review3, review4));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(review2.getId())
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(1),

                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(0), review1, store, user)
            );
        }

        @Test
        void 삭제된_리뷰는_조회되지_않는다() throws Exception {
            // given
            Review deletedReviewByUser = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요", 5, ReviewStatus.DELETED);
            Review deletedReviewByAdmin = ReviewCreator.create(store.getId(), user.getId(), "너무 맛있어요", 5, ReviewStatus.FILTERED);
            reviewRepository.saveAll(List.of(deletedReviewByAdmin, deletedReviewByUser));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).isEmpty()
            );
        }

        @Test
        void 삭제된_가게인경우_없어진_가게라고_표시된다() throws Exception {
            // given
            Store storeDeletedByAdmin = StoreCreator.createDefaultWithMenu(user.getId(), "삭제되기 전 가게 이름", StoreStatus.DELETED);
            Store storeDeleteByUser = StoreCreator.createDefaultWithMenu(user.getId(), "삭제되기 전 가게 이름", StoreStatus.FILTERED);
            storeRepository.saveAll(List.of(storeDeleteByUser, storeDeletedByAdmin));

            Review review1 = ReviewCreator.create(storeDeleteByUser.getId(), user.getId(), "너무 맛있어요", 5);
            Review review2 = ReviewCreator.create(storeDeletedByAdmin.getId(), user.getId(), "너무 맛있어요", 5);
            reviewRepository.saveAll(List.of(review1, review2));

            RetrieveMyReviewsRequest request = RetrieveMyReviewsRequest.testBuilder()
                .size(2)
                .cursor(null)
                .build();

            // when
            ApiResponse<ReviewsCursorResponse> response = reviewMockApiCaller.retrieveMyReviewHistories(request, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData().getNextCursor()).isEqualTo(-1),
                () -> assertThat(response.getData().isHasNext()).isFalse(),
                () -> assertThat(response.getData().getContents()).hasSize(2),
                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(0), review2, storeDeletedByAdmin, user),
                () -> assertReviewDetailInfoResponse(response.getData().getContents().get(1), review1, storeDeleteByUser, user)
            );
        }

    }

}
