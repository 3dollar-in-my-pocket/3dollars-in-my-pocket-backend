package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.service.medal.AddUserMedalFacadeService;
import com.depromeet.threedollar.domain.event.review.ReviewCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreDeletedEvent;
import com.depromeet.threedollar.domain.event.user.NewUserCreatedEvent;
import com.depromeet.threedollar.domain.event.visit.VisitHistoryAddedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddUserMedalEventListener {

    private final AddUserMedalFacadeService userMedalFacadeService;

    @EventListener
    public void addObtainableMedalsByAddStore(StoreCreatedEvent event) {
        userMedalFacadeService.addObtainableMedalsByAddStore(event.getUserId());
    }

    @EventListener
    public void addObtainableMedalsByDeleteStore(StoreDeletedEvent event) {
        userMedalFacadeService.addObtainableMedalsByDeleteStore(event.getUserId());
    }

    @EventListener
    public void addObtainableMedalsByAddReview(ReviewCreatedEvent event) {
        userMedalFacadeService.addObtainableMedalsByAddReview(event.getUserId());
    }

    @EventListener
    public void addObtainableMedalsByVisitStore(VisitHistoryAddedEvent event) {
        userMedalFacadeService.addObtainableMedalsByVisitStore(event.getUserId());
    }

    @EventListener
    public void addDefaultMedal(NewUserCreatedEvent event) {
        userMedalFacadeService.addAndActivateDefaultMedals(event.getUserId());
    }

}
