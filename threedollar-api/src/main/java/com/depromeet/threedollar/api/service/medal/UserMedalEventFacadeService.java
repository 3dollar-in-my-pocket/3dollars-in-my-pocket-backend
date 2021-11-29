package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.depromeet.threedollar.domain.domain.medal.UserMedalType.MedalAcquisitionCondition.*;

@RequiredArgsConstructor
@Component
public class UserMedalEventFacadeService {

    private final UserMedalEventService userMedalEventService;

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final StoreDeleteRequestRepository storeDeleteRequestRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    public void addObtainableMedalsByAddStore(Long userId) {
        userMedalEventService.addMedalsIfMatchCondition(userId, ADD_STORE, () -> storeRepository.findCountsByUserId(userId));
    }

    public void addObtainableMedalsByDeleteStore(Long userId) {
        userMedalEventService.addMedalsIfMatchCondition(userId, DELETE_STORE, () -> storeDeleteRequestRepository.findCountsByUserId(userId));
    }

    public void addObtainableMedalsByAddReview(Long userId) {
        userMedalEventService.addMedalsIfMatchCondition(userId, ADD_REVIEW, () -> reviewRepository.findCountsByUserId(userId));
    }

    public void addObtainableMedalsByVisitStore(Long userId) {
        userMedalEventService.addMedalsIfMatchCondition(userId, VISIT_STORE, () -> visitHistoryRepository.findCountsByUserId(userId));
        userMedalEventService.addMedalsIfMatchCondition(userId, VISIT_NOT_EXISTS_STORE, () -> visitHistoryRepository.findCountsByUserIdAndVisitType(userId, VisitType.NOT_EXISTS));
    }

}
