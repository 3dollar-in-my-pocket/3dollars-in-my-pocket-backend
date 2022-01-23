package com.depromeet.threedollar.external.client.kakao.dto.response;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class KaKaoAccountResponse {

    private String email;

    public static KaKaoAccountResponse testInstance(String email) {
        return new KaKaoAccountResponse(email);
    }

}
