package com.depromeet.threedollar.api.vendor.service.medal;

import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_MEDALS;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.vendor.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.api.vendor.service.user.UserServiceUtils;
import com.depromeet.threedollar.api.vendor.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.api.vendor.service.user.dto.response.UserMedalResponse;
import com.depromeet.threedollar.domain.rds.vendor.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.User;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserMedalService {

    private final UserRepository userRepository;

    @Cacheable(cacheNames = USER_MEDALS, key = "#userId")
    @Transactional(readOnly = true)
    public List<UserMedalResponse> retrieveObtainedMedals(Long userId) {
        List<UserMedal> userMedals = UserServiceUtils.findUserById(userRepository, userId).getUserMedals();
        return userMedals.stream()
            .map(UserMedalResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserInfoResponse updateRepresentativeMedal(ChangeRepresentativeMedalRequest request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        user.updateActivatedMedal(request.getMedalId());
        return UserInfoResponse.of(user);
    }

}
