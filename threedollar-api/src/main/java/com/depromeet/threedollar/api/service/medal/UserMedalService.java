package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.api.service.user.dto.request.ActivateRepresentativeMedalRequest;
import com.depromeet.threedollar.api.service.medal.dto.response.MedalResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
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
    public void addMedalsIfSatisfyCondition(Long userId, MedalAcquisitionConditionType conditionType, Supplier<Long> findCountsByUser) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        List<Medal> medalsUserNotHeldByCondition = getMedalsUserNotHeldByCondition(user, conditionType);
        if (hasNoMoreMedalsCanBeObtained(medalsUserNotHeldByCondition)) {
            return;
        }
        user.addMedals(filterMedalsSatisfyCondition(medalsUserNotHeldByCondition, conditionType, findCountsByUser.get()));
    }

    private List<Medal> getMedalsUserNotHeldByCondition(User user, MedalAcquisitionConditionType conditionType) {
        List<Medal> medals = medalRepository.findAllByConditionType(conditionType);
        List<Long> medalsUserObtains = user.getUserMedals().stream()
            .map(UserMedal::getMedalId)
            .collect(Collectors.toList());
        return medals.stream()
            .filter(medal -> !medalsUserObtains.contains(medal.getId()))
            .collect(Collectors.toList());
    }

    private boolean hasNoMoreMedalsCanBeObtained(List<Medal> medals) {
        return medals.isEmpty();
    }

    private List<Medal> filterMedalsSatisfyCondition(List<Medal> medals, MedalAcquisitionConditionType conditionType, long counts) {
        return medals.stream()
            .filter(medal -> medal.canObtain(conditionType, counts))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedalResponse> retrieveObtainedMedals(Long userId) {
        List<UserMedal> userMedals = UserServiceUtils.findUserById(userRepository, userId).getUserMedals();
        return userMedals.stream()
            .map(MedalResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserInfoResponse updateRepresentativeMedal(ActivateRepresentativeMedalRequest request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        user.updateActivatedMedal(request.getMedalId());
        return UserInfoResponse.of(user);
    }

}
