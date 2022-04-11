package com.depromeet.threedollar.external.client.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;

@FeignClient(
    name = "kakaoAuthApiClient",
    url = "${external.client.kakao.profile.base-url}",
    configuration = {
        KaKaoFeignConfig.class
    }
)
public interface KaKaoAuthApiClient {

    @GetMapping("${external.client.kakao.profile.url}")
    KaKaoProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);

}
