package com.depromeet.threedollar.api.vendor.listener.store;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.api.vendor.service.store.StoreRatingService;
import com.depromeet.threedollar.domain.rds.vendor.event.review.ReviewChangedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StoreRatingEventListener {

    private final StoreRatingService storeRatingService;

    @Async
    @EventListener
    public void renewStoreRating(ReviewChangedEvent event) {
        storeRatingService.renewStoreAverageRating(event.getStoreId());
    }

}
