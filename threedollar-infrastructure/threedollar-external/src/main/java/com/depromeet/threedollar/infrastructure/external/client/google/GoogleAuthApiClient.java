package com.depromeet.threedollar.infrastructure.external.client.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.infrastructure.external.client.google.dto.response.GoogleProfileInfoResponse;

@FeignClient(
    name = "GoogleAuthApiClient",
    url = "${external.client.google.profile.base-url}",
    configuration = {
        GoogleFeignConfig.class
    }
)
public interface GoogleAuthApiClient {

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @GetMapping("${external.client.google.profile.url}")
    GoogleProfileInfoResponse getProfileInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

}
