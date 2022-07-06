package com.depromeet.threedollar.external.client.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileResponse;

@FeignClient(
    name = "NaverAuthApiClient",
    url = "${external.client.naver.profile.base-url}",
    configuration = {
        NaverFeignConfig.class
    }
)
public interface NaverAuthApiClient {

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @GetMapping("${external.client.naver.profile.url}")
    NaverProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);

}
