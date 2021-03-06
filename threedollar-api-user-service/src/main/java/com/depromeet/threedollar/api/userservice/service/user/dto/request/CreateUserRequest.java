package com.depromeet.threedollar.api.userservice.service.user.dto.request;

import com.depromeet.threedollar.api.userservice.config.vadlidator.NickName;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank(message = "{user.socialId.notBlank}")
    private String socialId;

    @NotNull(message = "{user.socialType.notnull}")
    private UserSocialType socialType;

    @NickName
    private String name;

    @Builder
    private CreateUserRequest(String socialId, UserSocialType socialType, String name) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.name = name;
    }

    public User toEntity() {
        return User.newInstance(socialId, socialType, name);
    }

}
