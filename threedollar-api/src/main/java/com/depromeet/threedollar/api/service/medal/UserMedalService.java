package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.domain.medal.Medal;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserMedalService {

    private final UserRepository userRepository;
    private final MedalRepository medalRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addMedalsIfMatchCondition(Long userId, MedalAcquisitionConditionType conditionType, Supplier<Long> findCountsByUser) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        List<Medal> medalsNotOwned = getNoOwnedMedalsByCondition(user, conditionType);
        if (hasNoMoreMedalsCanBeObtained(medalsNotOwned)) {
            return;
        }
        user.addMedals(newUserMedalsCanBeObtained(medalsNotOwned, conditionType, findCountsByUser.get()));
    }

    private List<Medal> getNoOwnedMedalsByCondition(User user, MedalAcquisitionConditionType conditionType) {
        List<Medal> medals = medalRepository.findAllByConditionType(conditionType);
        List<Medal> hasMedals = user.getUserMedals().stream()
            .map(UserMedal::getMedal)
            .collect(Collectors.toList());
        return medals.stream()
            .filter(medal -> !hasMedals.contains(medal))
            .collect(Collectors.toList());
    }

    private boolean hasNoMoreMedalsCanBeObtained(List<Medal> medals) {
        return medals.isEmpty();
    }

    private List<Medal> newUserMedalsCanBeObtained(List<Medal> medals, MedalAcquisitionConditionType conditionType, long counts) {
        return medals.stream()
            .filter(medal -> medal.canObtain(conditionType, counts))
            .collect(Collectors.toList());
    }

}
