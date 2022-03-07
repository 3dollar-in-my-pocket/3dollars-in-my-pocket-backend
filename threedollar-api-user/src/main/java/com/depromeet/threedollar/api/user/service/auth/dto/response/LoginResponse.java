package com.depromeet.threedollar.api.user.service.auth.dto.response;

import lombok.*;

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
