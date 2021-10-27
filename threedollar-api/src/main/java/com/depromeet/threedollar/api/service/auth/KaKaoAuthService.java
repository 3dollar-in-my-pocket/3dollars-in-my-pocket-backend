package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.user.request.LoginRequest;
import com.depromeet.threedollar.api.service.user.request.SignUpRequest;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.common.utils.HttpHeaderUtils;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.external.client.kakao.KaKaoApiClient;
import com.depromeet.threedollar.external.client.kakao.dto.response.KaKaoProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KaKaoAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.KAKAO;

    private final KaKaoApiClient kaKaoApiCaller;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Long signUp(SignUpRequest request) {
        KaKaoProfileResponse response = kaKaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        return userService.createUser(request.toCreateUserRequest(response.getId()));
    }

    @Override
    public Long login(LoginRequest request) {
        KaKaoProfileResponse response = kaKaoApiCaller.getProfileInfo(HttpHeaderUtils.withBearerToken(request.getToken()));
        return UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType).getId();
    }

}
