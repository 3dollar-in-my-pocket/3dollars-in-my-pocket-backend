package com.depromeet.threedollar.api.user.service.store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreRatingService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void renewStoreAverageRating(Long storeId) {
        Store store = StoreServiceUtils.findStoreById(storeRepository, storeId);
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
