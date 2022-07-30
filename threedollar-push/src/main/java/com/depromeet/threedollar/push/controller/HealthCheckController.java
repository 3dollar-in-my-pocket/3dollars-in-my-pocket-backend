package com.depromeet.threedollar.push.controller;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.common.constants.VersionConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/ping")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.OK;
    }

    @GetMapping("/version")
    public ApiResponse<String> version() {
        return ApiResponse.success(VersionConstants.VERSION);
    }

}
