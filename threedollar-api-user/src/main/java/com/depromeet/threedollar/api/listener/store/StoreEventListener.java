package com.depromeet.threedollar.api.listener.store;

import com.depromeet.threedollar.domain.user.event.review.ReviewChangedEvent;
import com.depromeet.threedollar.api.service.store.StoreRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreEventListener {

    private final StoreRatingService storeRatingService;

    @Async
    @EventListener
    public void renewStoreRating(ReviewChangedEvent event) {
        storeRatingService.renewStoreAverageRating(event.getStoreId());
    }

}
