package com.depromeet.threedollar.domain.domain.user;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreator {

    public static User create(String socialId, UserSocialType socialType, String name) {
        return User.newInstance(socialId, socialType, name);
    }

}
