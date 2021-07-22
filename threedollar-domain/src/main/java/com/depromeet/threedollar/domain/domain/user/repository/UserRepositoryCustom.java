package com.depromeet.threedollar.domain.domain.user.repository;

import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;

public interface UserRepositoryCustom {

    User findUserBySocialIdAndSocialType(String socialId, UserSocialType type);

    User findUserByName(String name);

    User findUserById(Long userId);

    User findUserByIdAndSocialType(Long userId, UserSocialType socialType);

}
