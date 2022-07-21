package com.depromeet.threedollar.infrastructure.external.client.naver;

import com.depromeet.threedollar.common.exception.model.BadGatewayException;
import com.depromeet.threedollar.infrastructure.external.client.naver.dto.response.NaverProfileResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.depromeet.threedollar.infrastructure.external.common.constants.CircuitBreakerConstants.DEFAULT_CIRCUIT;

@FeignClient(
    name = "NaverAuthApiClient",
    url = "${external.client.naver.profile.base-url}",
    configuration = {
        NaverFeignConfig.class
    }
)
public interface NaverAuthApiClient {

    @CircuitBreaker(name = DEFAULT_CIRCUIT)
    @Retryable(backoff = @Backoff(value = 1000), value = BadGatewayException.class)
    @GetMapping("${external.client.naver.profile.url}")
    NaverProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);

}
