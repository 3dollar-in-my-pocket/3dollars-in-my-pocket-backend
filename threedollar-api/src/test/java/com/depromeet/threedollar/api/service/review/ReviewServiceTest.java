package com.depromeet.threedollar.api.service.review;

import com.depromeet.threedollar.api.service.UserSetUpTest;
import com.depromeet.threedollar.api.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.review.ReviewStatus;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReviewServiceTest extends UserSetUpTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Store store;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        storeRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    @BeforeEach
    void setUpStore() {
        store = StoreCreator.create(userId, "디프만 붕어빵");
        storeRepository.save(store);
    }

    @Nested
    class 가게_리뷰_등록 {

        @Test
        void 새로운_리뷰_데이터가_추가된다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;
            AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), contents, rating);

            // when
            reviewService.addReview(request, userId);

            // then
            List<Review> reviewList = reviewRepository.findAll();
            assertThat(reviewList).hasSize(1);
            assertReview(reviewList.get(0), store.getId(), contents, rating, userId);
        }

        @Test
        void 해당하는_가게가_없는경우_NOT_FOUND_STORE_EXCEPTION() {
            // given
            Long storeId = 99999L;
            AddReviewRequest request = AddReviewRequest.testInstance(storeId, "리뷰", 3);

            // when & then
            assertThatThrownBy(() -> reviewService.addReview(request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 가게의_평균_리뷰_점수가_갱신된다1() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), contents, rating);

            // when
            reviewService.addReview(request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getRating()).isEqualTo(4.0);
        }

        @Test
        void 가게의_평균_리뷰_점수가_갱신된다2() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            reviewRepository.save(ReviewCreator.create(store.getId(), userId, "맛 없어요", 1));

            // when
            AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), contents, rating);

            // when
            reviewService.addReview(request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getRating()).isEqualTo(2.5); // (4 + 1) / 2 = 2.5
        }

        @Test
        void 가게의_평균_리뷰_점수가_소수점_둘째자리에서_반올림되서_갱신된다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 3;

            Review review1 = ReviewCreator.create(store.getId(), userId, "맛 없어요", 1);
            Review review2 = ReviewCreator.create(store.getId(), userId, "맛 없어요", 4);
            reviewRepository.saveAll(Arrays.asList(review1, review2));

            // when
            AddReviewRequest request = AddReviewRequest.testInstance(store.getId(), contents, rating);

            // when
            reviewService.addReview(request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getRating()).isEqualTo(2.7); // (1 + 3 + 4) / 3 = 2.66666 = 2.7
        }

    }

    @Nested
    class 가게_리뷰_수정 {

        @Test
        void 성공시_해당_리뷰_정보가_수정된다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testInstance(contents, rating);

            // when
            reviewService.updateReview(review.getId(), request, userId);

            // then
            List<Review> reviewList = reviewRepository.findAll();
            assertThat(reviewList).hasSize(1);
            assertReview(reviewList.get(0), store.getId(), contents, rating, userId);
        }

        @Test
        void 가게의_평균_리뷰_점수가_갱신된다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            Review review = ReviewCreator.create(store.getId(), userId, "맛 없어요", 1);
            reviewRepository.saveAll(Arrays.asList(ReviewCreator.create(store.getId(), userId, "맛 없어요", 2), review));

            // when
            UpdateReviewRequest request = UpdateReviewRequest.testInstance(contents, rating);

            // when
            reviewService.updateReview(review.getId(), request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getRating()).isEqualTo(3); // (2 + 4 / 2 = 3)
        }

        @Test
        void 해당하는_리뷰가_존재하지_않으면_NOT_FOUND_REVIEW_EXCEPTION() {
            // given
            UpdateReviewRequest request = UpdateReviewRequest.testInstance("content", 5);

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(999L, request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 해당하는_리뷰가_사용자가_작성하지_않았을경우_NOT_FOUND_REVIEW_EXCEPTION() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testInstance("content", 5);

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(review.getId(), request, 999L)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 가게_리뷰_삭제 {

        @Test
        void 성공시_상태_필드가_DELETED로_수정된다() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when
            reviewService.deleteReview(review.getId(), userId);

            // then
            List<Review> reviewList = reviewRepository.findAll();
            assertThat(reviewList).hasSize(1);
            assertThat(reviewList.get(0).getStatus()).isEqualTo(ReviewStatus.DELETED);
        }

        @Test
        void 리뷰가_삭제되면_남은_리뷰들로_평균_리뷰점수가_계산된다() {
            // given
            Review remainReview = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            Review deletedReview = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 5);
            reviewRepository.saveAll(Arrays.asList(remainReview, deletedReview));

            // when
            reviewService.deleteReview(deletedReview.getId(), userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getRating()).isEqualTo(3);
        }

        @Test
        void 가게_평균_리뷰_점수가_갱신되며_아무_리뷰가_없는경우_0점이_된다() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when
            reviewService.deleteReview(review.getId(), userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getRating()).isEqualTo(0);
        }

        @Test
        void 해당하는_리뷰가_없을경우_NOT_FOUND_EXCEPTION() {
            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(999L, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 해당하는_리뷰가_사용자가_작성하지_않았을경우_NOT_FOUND_EXCEPTION() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(review.getId(), 999L)).isInstanceOf(NotFoundException.class);
        }

    }

    private void assertReview(Review review, Long storeId, String contents, int rating, Long userId) {
        assertThat(review.getStoreId()).isEqualTo(storeId);
        assertThat(review.getContents()).isEqualTo(contents);
        assertThat(review.getRating()).isEqualTo(rating);
        assertThat(review.getUserId()).isEqualTo(userId);
    }

}
