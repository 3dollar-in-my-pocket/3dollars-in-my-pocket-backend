package com.depromeet.threedollar.api.user.service.auth.policy;

import com.depromeet.threedollar.api.user.service.auth.AuthService;
import com.depromeet.threedollar.api.user.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.user.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.user.service.user.UserService;
import com.depromeet.threedollar.api.user.service.user.UserServiceUtils;
import com.depromeet.threedollar.common.utils.HttpHeaderUtils;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient;
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KaKaoAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.KAKAO;

    private final KaKaoAuthApiClient kaKaoApiCaller;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Long signUp(SignUpRequest request) {
        KaKaoProfileResponse response = kaKaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        return userService.registerUser(request.toCreateUserRequest(response.getId()));
    }

    @Override
    public Long login(LoginRequest request) {
        KaKaoProfileResponse response = kaKaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        return UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType).getId();
    }

}
