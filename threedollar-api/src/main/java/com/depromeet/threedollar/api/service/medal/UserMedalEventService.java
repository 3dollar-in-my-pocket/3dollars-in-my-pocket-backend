package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.depromeet.threedollar.domain.domain.medal.UserMedalType.MedalAcquisitionCondition.*;

@RequiredArgsConstructor
@Service
public class UserMedalEventService {

    private final UserMedalService userMedalService;

    private final UserMedalRepository userMedalRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final StoreDeleteRequestRepository storeDeleteRequestRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAvailableMedalByAddStore(Long userId) {
        addAvailableMedalByCondition(userId, ADD_STORE, storeRepository.findCountsByUserId(userId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAvailableMedalByAddReview(Long userId) {
        addAvailableMedalByCondition(userId, ADD_REVIEW, reviewRepository.findCountsByUserId(userId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAvailableMedalByDeleteStore(Long userId) {
        addAvailableMedalByCondition(userId, DELETE_STORE, storeDeleteRequestRepository.findCountsByUserId(userId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAvailableMedalByVisitHistory(Long userId) {
        addAvailableMedalByCondition(userId, VISIT_STORE, visitHistoryRepository.findCountsByUserId(userId));
        addAvailableMedalByCondition(userId, VISIT_NOT_EXISTS_STORE, visitHistoryRepository.findCountsByuserIdAndVisitType(userId, VisitType.NOT_EXISTS));
    }

    private void addAvailableMedalByCondition(Long userId, UserMedalType.MedalAcquisitionCondition condition, long countsByUserId) {
        List<UserMedalType> medals = getNotHasMedalsByCondition(userId, condition);
        if (achieveAllMedals(medals)) {
            return;
        }

        for (UserMedalType medal : medals) {
            if (medal.canGetMedal(countsByUserId)) {
                userMedalService.addUserMedal(medal, userId);
            }
        }
    }

    private List<UserMedalType> getNotHasMedalsByCondition(Long userId, UserMedalType.MedalAcquisitionCondition conditions) {
        List<UserMedalType> userMedalTypes = userMedalRepository.findAllUserMedalTypeByUserId(userId);
        return Arrays.stream(UserMedalType.values())
            .filter(userMedalType -> userMedalType.isMatchCondition(conditions))
            .filter(userMedalType -> !userMedalTypes.contains(userMedalType))
            .collect(Collectors.toList());
    }

    private boolean achieveAllMedals(List<UserMedalType> medals) {
        return medals.isEmpty();
    }

}
