package com.depromeet.threedollar.api.user.service.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.api.user.config.vadlidator.NickName;
import com.depromeet.threedollar.api.user.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank(message = "{auth.token.notBlank}")
    private String token;

    @NickName
    private String name;

    @NotNull(message = "{user.socialType.notNull}")
    private UserSocialType socialType;

    @Builder(builderMethodName = "testBuilder")
    private SignUpRequest(String token, String name, UserSocialType socialType) {
        this.token = token;
        this.name = name;
        this.socialType = socialType;
    }

    public CreateUserRequest toCreateUserRequest(String socialId) {
        return CreateUserRequest.builder()
            .socialId(socialId)
            .socialType(socialType)
            .name(name)
            .build();
    }

}
