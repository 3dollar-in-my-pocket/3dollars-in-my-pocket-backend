package com.depromeet.threedollar.api.service.user.dto.response;

import com.depromeet.threedollar.api.service.medal.dto.response.UserMedalResponse;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private static final UserInfoResponse SIGN_OUT_USER = new UserInfoResponse(null, "사라진 제보자", null, null);

    private Long userId;
    private String name;
    private UserSocialType socialType;
    private UserMedalResponse medal;

    public static UserInfoResponse of(User user) {
        if (user == null) {
            return SIGN_OUT_USER;
        }
        return new UserInfoResponse(user.getId(), user.getName(), user.getSocialType(), UserMedalResponse.of(user.getActiveMedal()));
    }

    public static UserInfoResponse of(@Nullable Long userId, @Nullable String userName, @Nullable UserSocialType userSocialType, @Nullable UserMedal userMedal) {
        if (userId == null) {
            return SIGN_OUT_USER;
        }
        return new UserInfoResponse(userId, userName, userSocialType, UserMedalResponse.of(userMedal));
    }

}
