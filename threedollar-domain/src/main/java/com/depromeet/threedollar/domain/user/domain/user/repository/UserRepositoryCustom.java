package com.depromeet.threedollar.domain.user.domain.user.repository;

import com.depromeet.threedollar.domain.user.domain.user.User;
import com.depromeet.threedollar.domain.user.domain.user.UserSocialType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserRepositoryCustom {

    boolean existsByName(String name);

    boolean existsBySocialIdAndSocialType(String socialId, UserSocialType socialType);

    @Nullable
    User findUserBySocialIdAndSocialType(String socialId, UserSocialType type);

    @Nullable
    User findUserById(Long userId);

	List<User> findAllByUserId(List<Long> userIds);

}
