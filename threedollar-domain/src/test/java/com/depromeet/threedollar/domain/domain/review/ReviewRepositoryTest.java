package com.depromeet.threedollar.domain.domain.review;

import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserCreator;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Nested
    class findAllWithCreatorByStoreId {

        @ParameterizedTest
        @AutoSource
        void 가게_리뷰와_함께_리뷰_작성자_정보를_함께_조회한다(String socialId, UserSocialType socialType, String userName, String reviewContents) {
            User user = UserCreator.create(socialId, socialType, userName);
            userRepository.save(user);

            Store store = StoreCreator.create(user.getId(), "가게");
            storeRepository.save(store);

            Review review = ReviewCreator.create(store.getId(), user.getId(), reviewContents, 5);
            reviewRepository.save(review);

            // when
            List<ReviewWithWriterProjection> reviews = reviewRepository.findAllWithCreatorByStoreId(store.getId());

            // then
            assertThat(reviews).hasSize(1);
            assertReviewWithCreatorDto(reviews.get(0), review.getId(), review.getRating(), reviewContents, user.getId(), userName, socialType);
        }

    }

    private void assertReviewWithCreatorDto(ReviewWithWriterProjection review, Long reviewId, int rating, String contents, Long userId, String name, UserSocialType socialType) {
        assertThat(review.getReviewId()).isEqualTo(reviewId);
        assertThat(review.getRating()).isEqualTo(rating);
        assertThat(review.getContents()).isEqualTo(contents);
        assertThat(review.getUserId()).isEqualTo(userId);
        assertThat(review.getUserName()).isEqualTo(name);
        assertThat(review.getUserSocialType()).isEqualTo(socialType);
    }

}
