package com.depromeet.threedollar.api.userservice.service.user;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_NICKNAME;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_USER;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOT_FOUND_USER;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUtils {

    static void validateNotExistsUserName(UserRepository userRepository, String name) {
        if (userRepository.existsByName(name)) {
            throw new ConflictException(String.format("이미 등록된 닉네임 (%s) 입니다", name), CONFLICT_NICKNAME);
        }
    }

    static void validateNotExistsUser(UserRepository userRepository, String socialId, UserSocialType socialType) {
        if (userRepository.existsBySocialIdAndSocialType(socialId, socialType)) {
            throw new ConflictException(String.format("이미 가입한 유저(%s - %s) 입니다", socialId, socialType), CONFLICT_USER);
        }
    }

    @NotNull
    public static User findUserById(UserRepository userRepository, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) 입니다", userId), NOT_FOUND_USER);
        }
        return user;
    }

    @NotNull
    public static User findUserBySocialIdAndSocialType(UserRepository userRepository, String socialId, UserSocialType socialType) {
        User user = userRepository.findUserBySocialIdAndSocialType(socialId, socialType);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s-%s) 입니다", socialId, socialType), NOT_FOUND_USER);
        }
        return user;
    }

}
