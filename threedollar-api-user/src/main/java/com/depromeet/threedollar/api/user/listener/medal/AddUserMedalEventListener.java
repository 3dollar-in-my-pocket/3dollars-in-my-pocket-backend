package com.depromeet.threedollar.api.user.listener.medal;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.api.user.service.medal.AddUserMedalFacadeService;
import com.depromeet.threedollar.domain.rds.user.event.review.ReviewCreatedEvent;
import com.depromeet.threedollar.domain.rds.user.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.rds.user.event.store.StoreDeletedEvent;
import com.depromeet.threedollar.domain.rds.user.event.user.NewUserCreatedEvent;
import com.depromeet.threedollar.domain.rds.user.event.visit.VisitHistoryAddedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AddUserMedalEventListener {

    private final AddUserMedalFacadeService userMedalFacadeService;

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

    @EventListener
    public void addDefaultMedal(NewUserCreatedEvent event) {
        userMedalFacadeService.addAndActivateDefaultMedals(event.getUserId());
    }

}
