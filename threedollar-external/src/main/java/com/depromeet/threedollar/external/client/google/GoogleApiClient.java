package com.depromeet.threedollar.external.client.google;

import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleProfileApiClient", url = "https://www.googleapis.com")
public interface GoogleApiClient {

    @GetMapping("/oauth2/v2/userinfo")
    GoogleProfileInfoResponse getProfileInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

}
