package com.depromeet.threedollar.external.client.google;

import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "googleAuthApiClient",
    url = "${external.client.google.profile.base-url}",
    configuration = {
        GoogleFeignConfig.class
    }
)
public interface GoogleAuthApiClient {

    @GetMapping("${external.client.google.profile.url}")
    GoogleProfileInfoResponse getProfileInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

}
