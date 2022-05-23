package com.depromeet.threedollar.api.vendor.service.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private String token;

    private Long userId;

    public static LoginResponse of(String sessionId, Long userId) {
        return new LoginResponse(sessionId, userId);
    }

}
