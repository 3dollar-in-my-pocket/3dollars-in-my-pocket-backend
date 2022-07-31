package com.depromeet.threedollar.api.userservice.service.user;

import com.depromeet.threedollar.api.userservice.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserRetrieveService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = UserServiceHelper.findUserById(userRepository, userId);
        return UserInfoResponse.of(user);
    }

    @Transactional(readOnly = true)
    public void checkIsAvailableName(CheckAvailableNameRequest request) {
        UserServiceHelper.validateNotExistsUserName(userRepository, request.getName());
    }

}
