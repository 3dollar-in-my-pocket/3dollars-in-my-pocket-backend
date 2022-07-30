package com.depromeet.threedollar.api.userservice.listener.store;

import com.depromeet.threedollar.api.core.service.userservice.store.StoreRatingService;
import com.depromeet.threedollar.domain.rds.event.userservice.review.ReviewChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
