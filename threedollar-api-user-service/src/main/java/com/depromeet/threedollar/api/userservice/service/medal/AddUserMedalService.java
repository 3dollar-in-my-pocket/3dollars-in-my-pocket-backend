package com.depromeet.threedollar.api.userservice.service.medal;

import com.depromeet.threedollar.api.userservice.service.user.UserServiceHelper;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.collection.MedalObtainCollection;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.LongSupplier;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_MEDALS;
import static com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType.NO_CONDITION;

@RequiredArgsConstructor
@Service
public class AddUserMedalService {

    private final UserRepository userRepository;
    private final MedalRepository medalRepository;

    @CacheEvict(cacheNames = USER_MEDALS, key = "#userId")
    @Transactional
    public void addMedalsIfSatisfyCondition(Long userId, MedalAcquisitionConditionType conditionType, LongSupplier countsByUserSupplier) {
        User user = UserServiceHelper.findUserById(userRepository, userId);
        MedalObtainCollection medalObtainCollection = MedalObtainCollection.of(medalRepository.findAllByConditionType(conditionType), conditionType, user);
        if (medalObtainCollection.hasNoMoreMedalsCanBeObtained()) {
            return;
        }
        user.addMedals(medalObtainCollection.getSatisfyMedalsCanBeObtained(countsByUserSupplier.getAsLong()));
    }

    @Transactional
    public void addAndActivateDefaultMedals(Long userId) {
        User user = UserServiceHelper.findUserById(userRepository, userId);
        MedalObtainCollection medalObtainCollection = MedalObtainCollection.of(medalRepository.findAllByConditionType(NO_CONDITION), NO_CONDITION, user);
        if (medalObtainCollection.hasNoMoreMedalsCanBeObtained()) {
            return;
        }
        List<Medal> medalSatisfyCondition = medalObtainCollection.getSatisfyMedalsCanBeObtainedByDefault();
        user.addMedals(medalSatisfyCondition);
        user.updateActivatedMedal(medalSatisfyCondition.get(0).getId());
    }

}
