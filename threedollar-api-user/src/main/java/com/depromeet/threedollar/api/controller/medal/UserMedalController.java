package com.depromeet.threedollar.api.controller.medal;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.medal.UserMedalService;
import com.depromeet.threedollar.api.service.medal.dto.request.ChangeRepresentativeMedalRequest;
import com.depromeet.threedollar.api.service.user.dto.response.UserMedalResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserMedalController {

    private final UserMedalService userMedalService;

    @ApiOperation("[인증] 사용자가 보유중인 메달들을 조회한다")
    @Auth
    @GetMapping("/v1/user/medals")
    public ApiResponse<List<UserMedalResponse>> getMyObtainedMedals(
        @UserId Long userId
    ) {
        return ApiResponse.success(userMedalService.retrieveObtainedMedals(userId));
    }

    @ApiOperation("[인증] 사용자의 장착중인 메달을 수정한다")
    @Auth
    @PutMapping("/v1/user/medal")
    public ApiResponse<UserInfoResponse> changeRepresentativeMedal(
        @Valid @RequestBody ChangeRepresentativeMedalRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(userMedalService.updateRepresentativeMedal(request, userId));
    }

}
