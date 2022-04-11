package com.depromeet.threedollar.api.user.service.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.api.user.config.vadlidator.NickName;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank(message = "{user.socialId.notBlank}")
    private String socialId;

    @NotNull(message = "{user.socialType.notnull}")
    private UserSocialType socialType;

    @NickName
    private String name;

    public static CreateUserRequest of(String socialId, UserSocialType type, String name) {
        return new CreateUserRequest(socialId, type, name);
    }

    public static CreateUserRequest testInstance(String socialId, UserSocialType type, String name) {
        return new CreateUserRequest(socialId, type, name);
    }

    public User toEntity() {
        return User.newInstance(socialId, socialType, name);
    }

}
