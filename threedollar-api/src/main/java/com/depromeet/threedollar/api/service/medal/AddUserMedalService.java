package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.collection.medal.MedalUserObtainCollection;
import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class AddUserMedalService {

    private final UserRepository userRepository;
    private final MedalRepository medalRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addMedalsIfSatisfyCondition(Long userId, MedalAcquisitionConditionType conditionType, Supplier<Long> findCountsByUser) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        MedalUserObtainCollection collection = MedalUserObtainCollection.of(medalRepository.findAllByConditionType(conditionType), conditionType, user);
        if (collection.hasNoMoreMedalsCanBeObtained()) {
            return;
        }
        user.addMedals(collection.getSatisfyMedalsCanBeObtained(findCountsByUser.get()));
    }

    @Transactional
    public void addAndActivateDefaultMedals(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        MedalUserObtainCollection collection = MedalUserObtainCollection.of(medalRepository.findAllByConditionType(MedalAcquisitionConditionType.NO_CONDITION), MedalAcquisitionConditionType.NO_CONDITION, user);
        if (collection.hasNoMoreMedalsCanBeObtained()) {
            return;
        }
        List<Medal> medalSatisfyCondition = collection.getSatisfyMedalsCanBeObtainedByDefault();
        user.addMedals(medalSatisfyCondition);
        user.updateActivatedMedal(medalSatisfyCondition.get(0).getId());
    }

}