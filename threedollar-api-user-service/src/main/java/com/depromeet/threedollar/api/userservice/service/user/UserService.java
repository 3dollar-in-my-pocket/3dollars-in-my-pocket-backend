package com.depromeet.threedollar.api.userservice.service.user;

import org.hibernate.exception.LockAcquisitionException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.userservice.service.user.dto.request.CheckAvailableNameRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.WithdrawalUser;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.WithdrawalUserRepository;
import com.depromeet.threedollar.domain.rds.event.userservice.user.NewUserCreatedEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final WithdrawalUserRepository withdrawalUserRepository;

    @Retryable(maxAttempts = 2, backoff = @Backoff(value = 1000), value = LockAcquisitionException.class)
    @Transactional
    public Long registerUser(CreateUserRequest request) {
        UserServiceUtils.validateNotExistsUser(userRepository, request.getSocialId(), request.getSocialType());
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName());
        User user = userRepository.save(request.toEntity());
        eventPublisher.publishEvent(NewUserCreatedEvent.of(user.getId()));
        return user.getId();
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
