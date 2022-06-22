package com.depromeet.threedollar.external.client.apple.dto.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppleIdTokenPayload {

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

    private AppleIdTokenPayload(String sub, String email) {
        this.sub = sub;
        this.email = email;
    }

    public static AppleIdTokenPayload testInstance(String uid, String email) {
        return new AppleIdTokenPayload(uid, email);
    }

}
