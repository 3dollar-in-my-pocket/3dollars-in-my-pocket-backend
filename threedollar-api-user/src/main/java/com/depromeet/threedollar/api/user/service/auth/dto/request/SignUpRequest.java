package com.depromeet.threedollar.api.user.service.auth.dto.request;

import com.depromeet.threedollar.api.user.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.api.user.config.vadlidator.NickName;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank(message = "{auth.token.notBlank}")
    private String token;

    @NickName
    private String name;

    @NotNull(message = "{user.socialType.notNull}")
    private UserSocialType socialType;

    public static SignUpRequest testInstance(String token, String name, UserSocialType socialType) {
        return new SignUpRequest(token, name, socialType);
    }

    public CreateUserRequest toCreateUserRequest(String socialId) {
        return CreateUserRequest.of(socialId, socialType, name);
    }

}
