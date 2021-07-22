package com.depromeet.threedollar.api.controller;

import com.depromeet.threedollar.api.common.dto.ApiResponse;
import com.depromeet.threedollar.api.config.session.UserSession;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.depromeet.threedollar.api.config.session.SessionConstants.USER_SESSION;

@Profile({"local", "local-will", "dev"})
@RequiredArgsConstructor
@RestController
public class TestController {

    private static final User testUser = User.newInstance("test-uid", UserSocialType.KAKAO, "관리자 계정");

    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @ApiOperation("[테스트용] 테스트를 위한 토큰을 받아옵니다.")
    @GetMapping("/test-token")
    public ApiResponse<LoginResponse> getTestToken() {
        User user = userRepository.findUserBySocialIdAndSocialType(testUser.getSocialId(), testUser.getSocialType());
        if (user == null) {
            user = userRepository.save(testUser);
        }
        httpSession.setAttribute(USER_SESSION, UserSession.of(user.getId()));
        return ApiResponse.success(LoginResponse.of(httpSession.getId()));
    }

}
