package com.depromeet.threedollar.external.client.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.depromeet.threedollar.external.client.naver.dto.response.NaverProfileResponse;

@FeignClient(
    name = "naverAuthApiClient",
    url = "${external.client.naver.profile.base-url}",
    configuration = {
        NaverFeignConfig.class
    }
)
public interface NaverAuthApiClient {

    @GetMapping("${external.client.naver.profile.url}")
    NaverProfileResponse getProfileInfo(@RequestHeader("Authorization") String accessToken);

}
