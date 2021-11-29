package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserMedalEventService {

    private final UserMedalService userMedalService;

    private final UserMedalRepository userMedalRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addMedalsIfMatchCondition(Long userId, UserMedalType.MedalAcquisitionCondition condition, Supplier<Long> findCountsByUser) {
        List<UserMedalType> medalsCanBeObtained = getNotHasMedalsByCondition(userId, condition);
        if (medalsCanBeObtained.isEmpty()) {
            return;
        }
        for (UserMedalType medal : medalsCanBeObtained) {
            if (medal.canGetMedal(findCountsByUser.get())) {
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

}
