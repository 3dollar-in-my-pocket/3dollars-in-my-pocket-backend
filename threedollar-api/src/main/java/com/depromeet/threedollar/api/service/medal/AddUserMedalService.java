package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.collection.medal.MedalObtainCollection;
import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

import static com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.USER_MEDALS;
import static com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType.NO_CONDITION;

@RequiredArgsConstructor
@Service
public class AddUserMedalService {

    private final UserRepository userRepository;
    private final MedalRepository medalRepository;

    @CacheEvict(key = "#userId", value = USER_MEDALS)
    @Transactional
    public void addMedalsIfSatisfyCondition(Long userId, MedalAcquisitionConditionType conditionType, Supplier<Long> countsByUserSupplier) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        MedalObtainCollection medalObtainCollection = MedalObtainCollection.of(medalRepository.findAllByConditionType(conditionType), conditionType, user);
        if (medalObtainCollection.hasNoMoreMedalsCanBeObtained()) {
            return;
        }
        user.addMedals(medalObtainCollection.getSatisfyMedalsCanBeObtained(countsByUserSupplier.get()));
    }

    @Transactional
    public void addAndActivateDefaultMedals(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        MedalObtainCollection medalObtainCollection = MedalObtainCollection.of(medalRepository.findAllByConditionType(NO_CONDITION), NO_CONDITION, user);
        if (medalObtainCollection.hasNoMoreMedalsCanBeObtained()) {
            return;
        }
        List<Medal> medalSatisfyCondition = medalObtainCollection.getSatisfyMedalsCanBeObtainedByDefault();
        user.addMedals(medalSatisfyCondition);
        user.updateActivatedMedal(medalSatisfyCondition.get(0).getId());
    }

}
