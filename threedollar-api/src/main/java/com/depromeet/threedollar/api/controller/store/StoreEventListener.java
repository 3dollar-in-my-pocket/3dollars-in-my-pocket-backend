package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.event.ReviewChangedEvent;
import com.depromeet.threedollar.api.service.store.StoreRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreEventListener {

    private final StoreRatingService storeRatingService;

    @EventListener
    public void renewStoreRating(ReviewChangedEvent event) {
        storeRatingService.renewStoreRating(event.getStore());
    }

}
