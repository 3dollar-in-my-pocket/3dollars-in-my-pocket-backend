package com.depromeet.threedollar.external.client.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.external.client.apple.dto.response.ApplePublicKeyResponse;
import com.depromeet.threedollar.external.config.feign.FeignDefaultConfig;

@FeignClient(
    name = "AppleAuthApiClient",
    url = "${external.client.apple.profile.base-url}",
    configuration = {
        FeignDefaultConfig.class
    }
)
public interface AppleAuthApiClient {

    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @GetMapping("${external.client.apple.profile.url}")
    ApplePublicKeyResponse retrieveApplePublicKey();

}
