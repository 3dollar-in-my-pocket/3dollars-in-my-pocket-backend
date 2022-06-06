package com.depromeet.threedollar.api.user.service.review;

import static com.depromeet.threedollar.api.user.service.review.support.ReviewAssertions.assertReview;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.user.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.user.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.user.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewStatus;

@SpringBootTest
class ReviewServiceTest extends SetupStoreServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        reviewRepository.deleteAllInBatch();
    }

    @Nested
    class AddReviewTest {

        @Test
        void 가게에_새로운_리뷰를_작성합니다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(store.getId())
                .contents(contents)
                .rating(rating)
                .build();

            // when
            reviewService.addReview(request, userId);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertAll(
                () -> assertThat(reviews).hasSize(1),
                () -> assertReview(reviews.get(0), store.getId(), contents, rating, userId, ReviewStatus.POSTED)
            );
        }

        @Test
        void 가게에_새로운_리뷰를_작성할때_가게가_없는경우_NOTFOUND_에러가_발생합니다() {
            // given
            Long notFoundStoreId = -1L;

            AddReviewRequest request = AddReviewRequest.testBuilder()
                .storeId(notFoundStoreId)
                .contents("리뷰 내용\n음식이 너무 맛있어요")
                .rating(5)
                .build();

            // when & then
            assertThatThrownBy(() -> reviewService.addReview(request, userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class UpdateStoreReviewTest {

        @Test
        void 내가_작성한_리뷰를_수정합니다() {
            // given
            String contents = "우와 맛있어요";
            int rating = 4;

            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents(contents)
                .rating(rating)
                .build();

            // when
            reviewService.updateReview(review.getId(), request, userId);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertAll(
                () -> assertThat(reviews).hasSize(1),
                () -> assertReview(reviews.get(0), store.getId(), contents, rating, userId, ReviewStatus.POSTED)
            );
        }

        @Test
        void 존재하지_않는_리뷰를_수정하는경우_NOTFOUND_에러가_발생합니다() {
            // given
            long notFoundReviewId = -1L;

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents("contents")
                .rating(5)
                .build();

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(notFoundReviewId, request, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 내가_작성하지_않은_리뷰를_수정하는_경우_NOT_FOUND_에러가_발생한다() {
            // given
            long creatorId = 10000L;
            Review review = ReviewCreator.create(store.getId(), creatorId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            UpdateReviewRequest request = UpdateReviewRequest.testBuilder()
                .contents("contents")
                .rating(5)
                .build();

            // when & then
            assertThatThrownBy(() -> reviewService.updateReview(review.getId(), request, userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class DeleteStoreReviewTest {

        @Test
        void 내가_작성한_리뷰를_삭제하면_DELETED로_표기되고_삭제처리된다() {
            // given
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when
            reviewService.deleteReview(review.getId(), userId);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertAll(
                () -> assertThat(reviews).hasSize(1),
                () -> assertReview(reviews.get(0), store.getId(), review.getContents(), review.getRating(), userId, ReviewStatus.DELETED)
            );
        }

        @Test
        void 없는_리뷰를_삭제하려하면_NOT_FOUND_에러가_발생한다() {
            // given
            Long notFoundReviewId = -1L;

            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(notFoundReviewId, userId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 내가_작성하지_않은_리뷰를_삭제하려하면_NOT_FOUND에러가_발생한다() {
            // given
            Long notFoundUserId = -1L;
            Review review = ReviewCreator.create(store.getId(), userId, "너무 맛있어요", 3);
            reviewRepository.save(review);

            // when & then
            assertThatThrownBy(() -> reviewService.deleteReview(review.getId(), notFoundUserId)).isInstanceOf(NotFoundException.class);
        }

    }

}
