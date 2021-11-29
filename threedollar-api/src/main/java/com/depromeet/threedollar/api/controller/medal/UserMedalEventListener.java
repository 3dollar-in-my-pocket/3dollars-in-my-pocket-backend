package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.service.medal.UserMedalFacadeService;
import com.depromeet.threedollar.domain.event.review.ReviewCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreDeletedEvent;
import com.depromeet.threedollar.domain.event.visit.VisitHistoryAddedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMedalEventListener {

    private final UserMedalFacadeService userMedalFacadeService;

    @Async
    @EventListener
    public void addObtainableMedalsByAddStore(StoreCreatedEvent event) {
        userMedalFacadeService.addObtainableMedalsByAddStore(event.getUserId());
    }

    @Async
    @EventListener
    public void addObtainableMedalsByDeleteStore(StoreDeletedEvent event) {
        userMedalFacadeService.addObtainableMedalsByDeleteStore(event.getUserId());
    }

    @Async
    @EventListener
    public void addObtainableMedalsByAddReview(ReviewCreatedEvent event) {
        userMedalFacadeService.addObtainableMedalsByAddReview(event.getUserId());
    }

    @Async
    @EventListener
    public void addObtainableMedalsByVisitStore(VisitHistoryAddedEvent event) {
        userMedalFacadeService.addObtainableMedalsByVisitStore(event.getUserId());
    }

}
