package com.depromeet.threedollar.api.service.user;

import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.common.exception.ConflictException;
import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtils {

    static void validateNotExistsUserName(UserRepository userRepository, String name) {
        User user = userRepository.findUserByName(name);
        if (user != null) {
            throw new ConflictException(String.format("이미 존재하는 닉네임 (%s) 입니다", name));
        }
    }

    public static User findUserById(UserRepository userRepository, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) 입니다", userId), ErrorCode.NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }

    public static User findUserByIdAndSocialType(UserRepository userRepository, Long userId, UserSocialType socialType) {
        User user = userRepository.findUserByIdAndSocialType(userId, socialType);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s-%s) 입니다", userId, socialType), ErrorCode.NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }

    public static User findUserBySocialIdAndSocialType(UserRepository userRepository, String socialId, UserSocialType socialType) {
        User user = userRepository.findUserBySocialIdAndSocialType(socialId, socialType);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s - %s) 입니다", socialId, socialType), ErrorCode.NOT_FOUND_USER_EXCEPTION);
        }
        return user;
    }

}
