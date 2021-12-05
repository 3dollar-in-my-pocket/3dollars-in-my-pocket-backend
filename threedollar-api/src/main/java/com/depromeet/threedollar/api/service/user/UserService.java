package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.api.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.WithdrawalUser;
import com.depromeet.threedollar.domain.domain.user.WithdrawalUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WithdrawalUserRepository withdrawalUserRepository;

    @Transactional
    public Long registerUser(CreateUserRequest request) {
        UserServiceUtils.validateNotExistsUser(userRepository, request.getSocialId(), request.getSocialType());
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName());
        return userRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        return UserInfoResponse.of(user);
    }

    @Transactional(readOnly = true)
    public void checkIsAvailableName(CheckAvailableNameRequest request) {
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName());
    }

    @Transactional
    public UserInfoResponse updateUserInfo(UpdateUserInfoRequest request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName());
        user.updateName(request.getName());
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void signOut(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        withdrawalUserRepository.save(WithdrawalUser.newInstance(user));
        userRepository.delete(user);
    }

}
