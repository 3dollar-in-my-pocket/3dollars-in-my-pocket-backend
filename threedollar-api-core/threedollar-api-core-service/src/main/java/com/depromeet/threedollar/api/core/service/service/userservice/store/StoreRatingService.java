package com.depromeet.threedollar.api.core.service.service.userservice.store;

import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StoreRatingService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void renewStoreAverageRating(Long storeId) {
        Store store = StoreServiceHelper.findStoreById(storeRepository, storeId);
        double average = calculateAverageRating(storeId);
        store.updateAverageRating(average);
        storeRepository.save(store);
    }

    private double calculateAverageRating(Long storeId) {
        return reviewRepository.findAllByStoreIdWithLock(storeId).stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0);
    }

}
