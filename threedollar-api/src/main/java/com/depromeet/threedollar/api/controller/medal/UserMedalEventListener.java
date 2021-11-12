package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.event.review.ReviewCreatedEvent;
import com.depromeet.threedollar.api.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.api.event.store.StoreDeletedEvent;
import com.depromeet.threedollar.api.event.visit.VisitHistoryAddedEvent;
import com.depromeet.threedollar.api.service.medal.UserMedalEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMedalEventListener {

    private final UserMedalEventService userMedalEventService;

    @Async
    @EventListener
    public void addAvailableMedalByAddStore(StoreCreatedEvent event) {
        userMedalEventService.addAvailableMedalByAddStore(event.getUserId());
    }

    @Async
    @EventListener
    public void addAvailableMedalByAddReview(ReviewCreatedEvent event) {
        userMedalEventService.addAvailableMedalByAddReview(event.getUserId());
    }

    @Async
    @EventListener
    public void addAvailableMedalByAddVisitHistory(VisitHistoryAddedEvent event) {
        userMedalEventService.addAvailableMedalByVisitHistory(event.getUserId());
    }

    @Async
    @EventListener
    public void addAvailableMedalByDeleteStore(StoreDeletedEvent event) {
        userMedalEventService.addAvailableMedalByDeleteStore(event.getUserId());
    }

}
