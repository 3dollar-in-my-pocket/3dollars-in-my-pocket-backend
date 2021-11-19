package com.depromeet.threedollar.api.service.user.dto.response;

import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private static final UserInfoResponse SIGN_OUT_USER = new UserInfoResponse(null, "사라진 제보자", null, null);

    private Long userId;
    private String name;
    private UserSocialType socialType;
    private UserMedalType medalType;

    public static UserInfoResponse of(User user) {
        if (user == null) {
            return SIGN_OUT_USER;
        }
        return new UserInfoResponse(user.getId(), user.getName(), user.getSocialType(), user.getMedalType());
    }

    public static UserInfoResponse of(Long userId, String userName, UserSocialType userSocialType, UserMedalType medalType) {
        if (userId == null) {
            return SIGN_OUT_USER;
        }
        return new UserInfoResponse(userId, userName, userSocialType, medalType);
    }

}
