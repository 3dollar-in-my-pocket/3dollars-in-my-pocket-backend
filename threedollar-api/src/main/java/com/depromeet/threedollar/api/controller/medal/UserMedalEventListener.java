package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.domain.event.review.ReviewCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreDeletedEvent;
import com.depromeet.threedollar.domain.event.visit.VisitHistoryAddedEvent;
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
    public void addObtainableMedalsByAddStore(StoreCreatedEvent event) {
        userMedalEventService.addObtainableMedalsByAddStore(event.getUserId());
    }

    @Async
    @EventListener
    public void addObtainableMedalsByDeleteStore(StoreDeletedEvent event) {
        userMedalEventService.addObtainableMedalsByDeleteStore(event.getUserId());
    }

    @Async
    @EventListener
    public void addObtainableMedalsByAddReview(ReviewCreatedEvent event) {
        userMedalEventService.addObtainableMedalsByAddReview(event.getUserId());
    }

    @Async
    @EventListener
    public void addObtainableMedalsByVisitStore(VisitHistoryAddedEvent event) {
        userMedalEventService.addObtainableMedalsByVisitStore(event.getUserId());
    }

}
