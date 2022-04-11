package com.depromeet.threedollar.external.client.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.depromeet.threedollar.external.client.apple.dto.response.ApplePublicKeyResponse;

@FeignClient(
    name = "appleAuthApiClient",
    url = "${external.client.apple.profile.base-url}"
)
public interface AppleAuthApiClient {

    @GetMapping("${external.client.apple.profile.url}")
    ApplePublicKeyResponse getAppleAuthPublicKey();

}
