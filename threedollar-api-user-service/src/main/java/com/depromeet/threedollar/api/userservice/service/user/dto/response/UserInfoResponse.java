package com.depromeet.threedollar.api.userservice.service.user.dto.response;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoResponse extends AuditingTimeResponse {

    private static final String SIGN_OUT_NICKNAME = "사라진 제보자";

    @Nullable
    private Long userId;

    private String name;

    @Nullable
    private UserSocialType socialType;

    private UserMedalResponse medal;

    public static UserInfoResponse of(@Nullable User user) {
        if (user == null) {
            return signOut();
        }
        UserInfoResponse response = new UserInfoResponse(user.getId(), user.getName(), user.getSocialType(), UserMedalResponse.of(user.getActivatedMedal()));
        response.setAuditingTimeByEntity(user);
        return response;
    }

    private static UserInfoResponse signOut() {
        return new UserInfoResponse(null, SIGN_OUT_NICKNAME, null, UserMedalResponse.signOut());
    }

}
