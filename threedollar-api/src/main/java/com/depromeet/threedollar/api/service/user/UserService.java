package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.api.service.user.dto.request.CheckDuplicateNameRequest;
import com.depromeet.threedollar.api.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.service.user.dto.request.UpdateUserInfoRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.domain.user.WithdrawalUserRepository;
import com.depromeet.threedollar.common.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WithdrawalUserRepository withdrawalUserRepository;

    @Transactional
    public Long createUser(CreateUserRequest request) {
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName());
        User user = userRepository.findUserBySocialIdAndSocialType(request.getSocialId(), request.getSocialType());
        if (user == null) {
            return signUp(request);
        }
        throw new ConflictException(String.format("이미 존재하는 유저 (%s - %s) 입니다", request.getSocialId(), request.getSocialType()));
    }

    private Long signUp(CreateUserRequest request) {
        return userRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        return UserInfoResponse.of(user);
    }

    @Transactional(readOnly = true)
    public void checkDuplicateName(CheckDuplicateNameRequest request) {
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName());
    }

    @Transactional
    public UserInfoResponse updateUserInfo(UpdateUserInfoRequest request, Long userId) {
        User user = UserServiceUtils.findUserById(userRepository, userId);
        UserServiceUtils.validateNotExistsUserName(userRepository, request.getName()); // TODO 차후 닉네임 공백 적용 여부 고민.
        user.update(request.getName());
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void signOut(Long userId, UserSocialType socialType) {
        User user = UserServiceUtils.findUserByIdAndSocialType(userRepository, userId, socialType);
        withdrawalUserRepository.save(user.signOut());
        userRepository.delete(user);
    }

}
