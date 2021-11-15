package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.SetupStoreServiceTest;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoreRatingServiceTest extends SetupStoreServiceTest {

    @Autowired
    private StoreRatingService storeRatingService;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        reviewRepository.deleteAll();
    }

    @Test
    void 가게의_평균점수를_갱신한다() {
        // given
        reviewRepository.saveAll(List.of(
            ReviewCreator.create(storeId, userId, "1점", 1),
            ReviewCreator.create(storeId, userId, "5점", 5)
        ));

        // when
        storeRatingService.renewStoreRating(store);

        // then
        List<Store> stores = storeRepository.findAll();
        assertThat(stores).hasSize(1);
        assertThat(stores.get(0).getRating()).isEqualTo(3.0); // (1 + 5) / 2 = 3
    }

    @Test
    void 가게의_평균점수를_갱신시_소수점_둘째자리에서_반올림된다() {
        // given
        reviewRepository.saveAll(List.of(
            ReviewCreator.create(storeId, userId, "1점", 1),
            ReviewCreator.create(storeId, userId, "3점", 3),
            ReviewCreator.create(storeId, userId, "4점", 4)
        ));

        // when
        storeRatingService.renewStoreRating(store);

        // then
        List<Store> stores = storeRepository.findAll();
        assertThat(stores).hasSize(1);
        assertThat(stores.get(0).getRating()).isEqualTo(2.7); // (1 + 3 + 4) / 3 = 2.677
    }

    @Test
    void 아무런_리뷰가_없는경우_평균점수는_0점이_된다() {
        // when
        storeRatingService.renewStoreRating(store);

        // then
        List<Store> stores = storeRepository.findAll();
        assertThat(stores).hasSize(1);
        assertThat(stores.get(0).getRating()).isEqualTo(0);
    }

}
