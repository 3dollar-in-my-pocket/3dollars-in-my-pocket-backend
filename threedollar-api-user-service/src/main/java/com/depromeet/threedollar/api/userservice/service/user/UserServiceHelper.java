package com.depromeet.threedollar.api.userservice.service.user;

import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.collection.UserDictionary;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E404_NOT_EXISTS_USER;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E409_DUPLICATE_NICKNAME;
import static com.depromeet.threedollar.common.exception.type.ErrorCode.E409_DUPLICATE_USER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceHelper {

    static void validateNotExistsUserName(UserRepository userRepository, String name) {
        if (userRepository.existsUserByName(name)) {
            throw new ConflictException(String.format("이미 등록된 닉네임 (%s) 입니다", name), E409_DUPLICATE_NICKNAME);
        }
    }

    static void validateNotExistsUser(UserRepository userRepository, String socialId, UserSocialType socialType) {
        if (userRepository.existsUserBySocialIdAndSocialType(socialId, socialType)) {
            throw new ConflictException(String.format("이미 가입한 유저(%s - %s) 입니다", socialId, socialType), E409_DUPLICATE_USER);
        }
    }

    @NotNull
    public static User findUserById(UserRepository userRepository, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s) 입니다", userId), E404_NOT_EXISTS_USER);
        }
        return user;
    }

    @NotNull
    public static User findUserBySocialIdAndSocialType(UserRepository userRepository, String socialId, UserSocialType socialType) {
        User user = userRepository.findUserBySocialIdAndSocialType(socialId, socialType);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%s-%s) 입니다", socialId, socialType), E404_NOT_EXISTS_USER);
        }
        return user;
    }

    @NotNull
    public static UserDictionary findUserDictionary(UserRepository userRepository, List<Long> userIds) {
        return UserDictionary.of(userRepository.findAllByUserId(userIds));
    }

}
