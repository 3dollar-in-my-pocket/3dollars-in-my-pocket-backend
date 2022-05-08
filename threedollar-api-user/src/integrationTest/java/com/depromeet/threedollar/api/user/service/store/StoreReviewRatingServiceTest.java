package com.depromeet.threedollar.api.user.service.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.user.service.SetupStoreServiceTest;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;

@SpringBootTest
class StoreReviewRatingServiceTest extends SetupStoreServiceTest {

    @Autowired
    private StoreRatingService storeRatingService;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        reviewRepository.deleteAllInBatch();
    }

    @DisplayName("리뷰 평균 점수 계산: 1점 + 5점 / 2 = 3점")
    @Test
    void 가게에_작성된_리뷰로_가게의_평균점수를_갱신한다() {
        // given
        reviewRepository.saveAll(List.of(
            ReviewCreator.builder()
                .storeId(storeId)
                .userId(userId)
                .contents("너무 맛없어요")
                .rating(1)
                .build(),
            ReviewCreator.builder()
                .storeId(storeId)
                .userId(userId)
                .contents("너무 맛있어요")
                .rating(5)
                .build()
        ));

        // when
        storeRatingService.renewStoreAverageRating(store.getId());

        // then
        List<Store> stores = storeRepository.findAll();
        assertAll(
            () -> assertThat(stores).hasSize(1),
            () -> assertThat(stores.get(0).getRating()).isEqualTo(3.0)
        );
    }

    @Test
    void 아무런_리뷰가_없는경우_평균점수는_0점이_된다() {
        // when
        storeRatingService.renewStoreAverageRating(store.getId());

        // then
        List<Store> stores = storeRepository.findAll();
        assertAll(
            () -> assertThat(stores).hasSize(1),
            () -> assertThat(stores.get(0).getRating()).isEqualTo(0)
        );
    }

}
