package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.common.utils.MathUtils;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StoreRatingService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void renewStoreRating(Store store) {
        double average = calculateAverageRating(store.getId());
        store.updateAverageRating(MathUtils.round(average, 1));
        storeRepository.save(store);
    }

    private double calculateAverageRating(Long storeId) {
        return reviewRepository.findAllByStoreIdWithLock(storeId).stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0);
    }

}
