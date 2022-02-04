package com.depromeet.threedollar.external.client.naver;

import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverAuthApiClient", url = "https://openapi.naver.com")
public interface NaverAuthApiClient {

    @GetMapping("/v1/nid/me")
    NaverProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);

}
