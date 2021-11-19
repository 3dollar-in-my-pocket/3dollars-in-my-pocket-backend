package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.controller.store.StoreEventListener;
import com.depromeet.threedollar.api.event.ReviewChangedEvent;
import com.depromeet.threedollar.api.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.review.ReviewStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertReviewUtils.assertReview;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ReviewServiceTest extends SetupStoreServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @MockBean
    private StoreEventListener storeEventListener;

    @AfterEach
    void cleanUp() {
        reviewRepository.deleteAll();
        super.cleanup();
    }

    @Nested
    class 가게_리뷰_등록 {

        @Test
        void 가게에_새로운_리뷰를_작성한다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;
            AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), contents, rating);

            // when
            reviewService.addReview(request, userId);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertThat(reviews).hasSize(1);
            assertReview(reviews.get(0), store.getId(), contents, rating, userId, ReviewStatus.POSTED);
        }

        @Test
        void 없는_가게에_리뷰를_작성하면_NOT_FOUND_STORE_EXCEPTION() {
            // given
            Long notFoundStoreId = -1L;
            AddReviewRequest request = AddReviewRequest.testInstance(notFoundStoreId, "리뷰", 3);

            // when & then
            assertThatThrownBy(() -> reviewService.addReview(request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 리뷰_등록시_가게의_평균_리뷰점수가_갱신된다() {
            // given
            AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), "우와", 4);

            // when
            reviewService.addReview(request, userId);

            // then
            verify(storeEventListener, times(1)).renewStoreRating(any(ReviewChangedEvent.class));
        }

    }

    @Nested
    class 가게_리뷰_수정 {

        @Test
        void 가게에_사용자가_작성한_리뷰를_수정한다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testInstance(contents, rating);

            // when
            reviewService.updateReview(review.getId(), request, userId);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertThat(reviews).hasSize(1);
            assertReview(reviews.get(0), store.getId(), contents, rating, userId, ReviewStatus.POSTED);
        }

        @Test
        void 없는_리뷰에_수정하려하면_NOT_FOUND_REVIEW_EXCEPTION() {
            // given
            Long notFoundReviewId = -1L;
            UpdateReviewRequest request = UpdateReviewRequest.testInstance("content", 5);

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(notFoundReviewId, request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 내가_작성하지_않은_리뷰를_수정하려하면_NOT_FOUND_REVIEW_EXCEPTION() {
            // given
            Long creatorId = 10000L;
            Review review = ReviewCreator.create(store.getId(), creatorId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testInstance("content", 5);

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(review.getId(), request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 리뷰를_수정하면_가게의_평균_리뷰점수가_갱신된다() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "맛 없어요", 2);
            reviewRepository.save(review);

            // when
            UpdateReviewRequest request = UpdateReviewRequest.testInstance("우와", 4);

            // when
            reviewService.updateReview(review.getId(), request, userId);

            // then
            verify(storeEventListener, times(1)).renewStoreRating(any(ReviewChangedEvent.class));
        }

    }

    @Nested
    class 가게_리뷰_삭제 {

        @Test
        void 사용자가_작성한_가게_삭제시_DELETED로_변경된다() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when
            reviewService.deleteReview(review.getId(), userId);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertThat(reviews).hasSize(1);
            assertReview(reviews.get(0), store.getId(), review.getContents(), review.getRating(), userId, ReviewStatus.DELETED);
        }

        @Test
        void 없는_리뷰에_삭제요청시_NOT_FOUND_EXCEPTION() {
            // given
            Long notFoundReviewId = -1L;

            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(notFoundReviewId, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 내가_작성하지_않은_리뷰에_삭제요청시_NOT_FOUND_EXCEPTION() {
            // given
            Long notFoundUserId = -1L;
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(review.getId(), notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 리뷰를_삭제하면_가게의_평균_리뷰점수가_갱신된다() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 5);
            reviewRepository.save(review);

            // when
            reviewService.deleteReview(review.getId(), userId);

            // then
            verify(storeEventListener, times(1)).renewStoreRating(any(ReviewChangedEvent.class));
        }

    }

}
