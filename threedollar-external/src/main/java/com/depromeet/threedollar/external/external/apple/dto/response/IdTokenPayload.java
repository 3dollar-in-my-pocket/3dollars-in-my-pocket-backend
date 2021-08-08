package com.depromeet.threedollar.external.external.apple.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdTokenPayload {

    private String iss;

    private String aud;

    private Long exp;

    private Long iat;

    private String sub;

    private String c_hash;

    private Long auth_time;

    private Boolean nonce_supported;

    private Boolean email_verified;

    private String email;

    private IdTokenPayload(String sub) {
        this.sub = sub;
    }

    public static IdTokenPayload testInstance(String sub) {
        return new IdTokenPayload(sub);
    }

}