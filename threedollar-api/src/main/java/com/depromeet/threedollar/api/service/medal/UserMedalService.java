package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.medal.dto.request.ActivateUserMedalRequest;
import com.depromeet.threedollar.api.service.medal.dto.response.UserMedalResponse;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_MEDAL_EXCEPTION;
import static com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.USER_AVAILABLE_MEDALS;
import static com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.USER_MEDALS_COUNTS;

@RequiredArgsConstructor
@Service
public class UserMedalService {

    private final UserRepository userRepository;
    private final UserMedalRepository userMedalRepository;

    @CacheEvict(key = "#userId", value = {USER_MEDALS_COUNTS, USER_AVAILABLE_MEDALS})
    @Transactional
    public void addUserMedal(UserMedalType type, Long userId) {
        if (userMedalRepository.existsMedalByUserId(userId, type)) {
            return;
        }
        userMedalRepository.save(UserMedal.of(userId, type));
    }

    @Cacheable(key = "#userId", value = USER_AVAILABLE_MEDALS)
    @Transactional(readOnly = true)
    public List<UserMedalResponse> getAvailableUserMedals(Long userId) {
        return userMedalRepository.findAllUserMedalTypeByUserId(userId).stream()
            .map(UserMedalResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserInfoResponse activateUserMedal(ActivateUserMedalRequest request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        validateIsAvailableMedal(userId, request.getMedalType());
        user.updateActiveMedal(request.getMedalType());
        return UserInfoResponse.of(user);
    }

    private void validateIsAvailableMedal(Long userId, UserMedalType medalType) {
        if (isNotAvailableMedal(userId, medalType)) {
            throw new NotFoundException(String.format("해당하는 유저 (%s)에게 메달 (%s)은 존재하지 않습니다", userId, medalType), NOT_FOUND_MEDAL_EXCEPTION);
        }
    }

    private boolean isNotAvailableMedal(Long userId, UserMedalType medalType) {
        if (medalType == null) {
            return false;
        }
        return !userMedalRepository.existsMedalByUserId(userId, medalType);
    }

}
