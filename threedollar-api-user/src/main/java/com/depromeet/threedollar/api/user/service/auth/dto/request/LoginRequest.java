package com.depromeet.threedollar.api.user.service.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "{auth.token.notBlank}")
    private String token;

    @NotNull(message = "{user.socialType.notNull}")
    private UserSocialType socialType;

    @Builder(builderMethodName = "testBuilder")
    private LoginRequest(String token, UserSocialType socialType) {
        this.token = token;
        this.socialType = socialType;
    }

}
