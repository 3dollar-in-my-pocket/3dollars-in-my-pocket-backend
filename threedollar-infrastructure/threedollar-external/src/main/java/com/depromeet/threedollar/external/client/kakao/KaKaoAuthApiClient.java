package com.depromeet.threedollar.external.client.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;

@FeignClient(
    name = "KakaoAuthApiClient",
    url = "${external.client.kakao.profile.base-url}",
    configuration = {
        KaKaoFeignConfig.class
    }
)
public interface KaKaoAuthApiClient {

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @GetMapping("${external.client.kakao.profile.url}")
    KaKaoProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);

}
