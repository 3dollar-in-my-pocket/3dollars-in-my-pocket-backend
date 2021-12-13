package com.depromeet.threedollar.api.service.user.dto.response;

import com.depromeet.threedollar.application.service.medal.dto.response.MedalResponse;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private static final String SIGN_OUT_NICKNAME = "사라진 제보자";

    private Long userId;
    private String name;
    private UserSocialType socialType;
    private MedalResponse medal;

    public static UserInfoResponse of(@Nullable User user) {
        if (user == null) {
            return signOut();
        }
        return new UserInfoResponse(user.getId(), user.getName(), user.getSocialType(), MedalResponse.of(user.getActivatedMedal()));
    }

    private static UserInfoResponse signOut() {
        return new UserInfoResponse(null, SIGN_OUT_NICKNAME, null, MedalResponse.signOut());
    }

}
