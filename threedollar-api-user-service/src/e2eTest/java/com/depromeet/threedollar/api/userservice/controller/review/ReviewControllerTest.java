package com.depromeet.threedollar.api.userservice.controller.review;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.SetupStoreControllerTest;
import com.depromeet.threedollar.api.userservice.listener.medal.AddUserMedalEventListener;
import com.depromeet.threedollar.api.userservice.listener.store.StoreRatingEventListener;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewsCursorResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.event.userservice.review.ReviewChangedEvent;
import com.depromeet.threedollar.domain.rds.event.userservice.review.ReviewCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.depromeet.threedollar.api.userservice.controller.review.support.ReviewAssertions.assertReviewDetailInfoResponse;
import static com.depromeet.threedollar.api.userservice.controller.review.support.ReviewAssertions.assertReviewInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @DisplayName("POST /api/v2/store/review")
    @Nested
    class AddNewReviewApiTest {

        @Test
        void ?????????_?????????_???????????????() throws Exception {
            // given
            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(store.getId())
                .contents("???????????? ?????? ????????????")
                .rating(5)
                .build();

            // when
            ApiResponse<ReviewInfoResponse> response = reviewMockApiCaller.addReview(request, token, 200);

            // then
            assertReviewInfoResponse(response.getData(), store.getId(), request.getContents(), request.getRating());
        }

        @Test
        void ??????_?????????_??????_??????_??????_????????????_???????????????() throws Exception {
            // given
            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(store.getId())
                .contents("??????")
                .rating(4)
                .build();

            // when
            reviewMockApiCaller.addReview(request, token, 200);

            // then
            verify(addUserMedalEventListener, times(1)).addObtainableMedalsByAddReview(any(ReviewCreatedEvent.class));
        }

        @Test
        void ?????????_????????????_?????????_??????_?????????_???????????????() throws Exception {
            // given
            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(store.getId())
                .contents("???????????? ?????? ????????????")
                .rating(5)
                .build();


            // when
            reviewMockApiCaller.addReview(request, token, 200);

            // then
            verify(storeRatingEventListener, times(1)).renewStoreRating(any(ReviewChangedEvent.class));
        }

    }

    @DisplayName("PUT /api/v2/store/review")
    @Nested
    class UpdateReviewApiTest {

        @Test
        void ??????_?????????_?????????_???????????????() throws Exception {
            // given
            Review review = ReviewFixture.create(store.getId(), user.getId());
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents("?????? ???????????????")
                .rating(1)
                .build();

            // when
            ApiResponse<ReviewInfoResponse> response = reviewMockApiCaller.updateReview(review.getId(), request, token, 200);

            // then
            assertReviewInfoResponse(response.getData(), store.getId(), request.getContents(), request.getRating());
        }

        @Test
        void ?????????_????????????_?????????_??????_???????????????_????????????() throws Exception {
            // given
            Review review = ReviewFixture.create(store.getId(), user.getId());
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents("??????")
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
        void ??????_?????????_?????????_???????????????() throws Exception {
            // given
            Review review = ReviewFixture.create(store.getId(), user.getId());
            reviewRepository.save(review);

            // when
            ApiResponse<String> response = reviewMockApiCaller.deleteReview(review.getId(), token, 200);

            // then
            assertThat(response.getData()).isEqualTo("OK");
        }

        @Test
        void ??????_?????????_?????????_????????????_?????????_??????_?????????_???????????????() throws Exception {
            // given
            Review review = ReviewFixture.create(store.getId(), user.getId());
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
        void ??????_?????????_??????_?????????_?????????_?????????_???????????????() throws Exception {
            // given
            Review review1 = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????1", 5);
            Review review2 = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????2", 4);
            Review review3 = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????3", 3);
            Review review4 = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????4", 2);
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
        void ??????_?????????_??????_?????????_?????????_?????????_???????????????() throws Exception {
            // given
            Review review1 = ReviewFixture.create(store.getId(), user.getId(), "????????????", 5);
            Review review2 = ReviewFixture.create(store.getId(), user.getId(), "????????????", 4);
            Review review3 = ReviewFixture.create(store.getId(), user.getId(), "?????? ?????????", 3);
            Review review4 = ReviewFixture.create(store.getId(), user.getId(), "??? ?????????", 2);
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
        void ??????_?????????_??????_?????????_????????????_??????_?????????_???_??????_?????????_??????_???????????????() throws Exception {
            // given
            Review review1 = ReviewFixture.create(store.getId(), user.getId());
            Review review2 = ReviewFixture.create(store.getId(), user.getId());
            Review review3 = ReviewFixture.create(store.getId(), user.getId());
            Review review4 = ReviewFixture.create(store.getId(), user.getId());
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
                () -> assertThat(response.getData().getContents()).hasSize(2)
            );
        }

        @Test
        void ??????_?????????_?????????_??????_???????????????_?????????_?????????_?????????_???????????????_SIZE_?????????_?????????_????????????_????????????() throws Exception {
            // given
            Review review1 = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????", 5);
            Review review2 = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????", 4);
            Review review3 = ReviewFixture.create(store.getId(), user.getId(), "??? ????????????", 3);
            Review review4 = ReviewFixture.create(store.getId(), user.getId(), "?????? ?????????", 2);
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
        void ??????_?????????_?????????_??????_???????????????_?????????_?????????_?????????_???????????????_SIZE??????_??????_?????????_???????????????() throws Exception {
            // given
            Review review1 = ReviewFixture.create(store.getId(), user.getId(), "??????! ????????????", 5);
            Review review2 = ReviewFixture.create(store.getId(), user.getId(), "??????! ????????????", 4);
            Review review3 = ReviewFixture.create(store.getId(), user.getId(), "???! ????????????", 3);
            Review review4 = ReviewFixture.create(store.getId(), user.getId(), "??????! ?????????", 2);
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
        void ??????_?????????_??????_?????????_????????????_?????????_?????????_????????????_?????????() throws Exception {
            // given
            Review deletedReviewByUser = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????", 5, ReviewStatus.DELETED);
            Review deletedReviewByAdmin = ReviewFixture.create(store.getId(), user.getId(), "?????? ????????????", 5, ReviewStatus.FILTERED);
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
        void ??????_?????????_??????_??????_?????????_?????????_?????????_???????????????_?????????_????????????_????????????() throws Exception {
            // given
            Store storeDeletedByAdmin = StoreFixture.createDefaultWithMenu(user.getId(), "???????????? ??? ?????? ??????", StoreStatus.DELETED);
            Store storeDeleteByUser = StoreFixture.createDefaultWithMenu(user.getId(), "???????????? ??? ?????? ??????", StoreStatus.FILTERED);
            storeRepository.saveAll(List.of(storeDeleteByUser, storeDeletedByAdmin));

            Review review1 = ReviewFixture.create(storeDeleteByUser.getId(), user.getId(), "?????? ????????????", 5);
            Review review2 = ReviewFixture.create(storeDeletedByAdmin.getId(), user.getId(), "?????? ????????????", 5);
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
