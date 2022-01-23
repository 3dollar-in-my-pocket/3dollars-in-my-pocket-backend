package com.depromeet.threedollar.external.client.kakao.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoProfileResponse {

    private String id;

    private KaKaoAccountResponse kakaoAccount;

    public static KaKaoProfileResponse testInstance(String socialId) {
        return new KaKaoProfileResponse(socialId, KaKaoAccountResponse.testInstance("test@kakao.com"));
    }

}
