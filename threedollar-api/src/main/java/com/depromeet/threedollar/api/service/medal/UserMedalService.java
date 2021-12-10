package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.medal.dto.request.ActivateRepresentativeMedalRequest;
import com.depromeet.threedollar.application.service.medal.dto.response.MedalResponse;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserMedalService {

    private final UserRepository userRepository;

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
