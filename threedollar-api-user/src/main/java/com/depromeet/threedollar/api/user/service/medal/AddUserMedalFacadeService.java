package com.depromeet.threedollar.api.user.service.medal;

import static com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType.ADD_REVIEW;
import static com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType.ADD_STORE;
import static com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType.DELETE_STORE;
import static com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE;
import static com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE;

import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AddUserMedalFacadeService {

    private final AddUserMedalService userMedalService;

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final StoreDeleteRequestRepository storeDeleteRequestRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    public void addObtainableMedalsByAddStore(Long userId) {
        userMedalService.addMedalsIfSatisfyCondition(userId, ADD_STORE, () -> storeRepository.countByUserId(userId));
    }

    public void addObtainableMedalsByDeleteStore(Long userId) {
        userMedalService.addMedalsIfSatisfyCondition(userId, DELETE_STORE, () -> storeDeleteRequestRepository.countsByUserId(userId));
    }

    public void addObtainableMedalsByAddReview(Long userId) {
        userMedalService.addMedalsIfSatisfyCondition(userId, ADD_REVIEW, () -> reviewRepository.countByUserId(userId));
    }

    public void addObtainableMedalsByVisitStore(Long userId) {
        userMedalService.addMedalsIfSatisfyCondition(userId, VISIT_BUNGEOPPANG_STORE, () -> visitHistoryRepository.countByUserIdAndMenuCategoryType(userId, MenuCategoryType.BUNGEOPPANG));
        userMedalService.addMedalsIfSatisfyCondition(userId, VISIT_NOT_EXISTS_STORE, () -> visitHistoryRepository.countByUserIdAndVisitType(userId, VisitType.NOT_EXISTS));
    }

    public void addAndActivateDefaultMedals(Long userId) {
        userMedalService.addAndActivateDefaultMedals(userId);
    }

}
