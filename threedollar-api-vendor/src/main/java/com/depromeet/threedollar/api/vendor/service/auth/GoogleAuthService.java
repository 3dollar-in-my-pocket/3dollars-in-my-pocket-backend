package com.depromeet.threedollar.api.vendor.service.auth;

import org.springframework.stereotype.Service;

import com.depromeet.threedollar.api.vendor.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.vendor.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.vendor.service.user.UserService;
import com.depromeet.threedollar.api.vendor.service.user.UserServiceUtils;
import com.depromeet.threedollar.common.utils.HttpHeaderUtils;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.UserSocialType;
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient;
import com.depromeet.threedollar.external.client.google.dto.response.GoogleProfileInfoResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class GoogleAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.GOOGLE;

    private final GoogleAuthApiClient googleAuthApiClient;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Long signUp(SignUpRequest request) {
        GoogleProfileInfoResponse response = googleAuthApiClient.getProfileInfo((HttpHeaderUtils.withBearerToken(request.getToken())));
        return userService.registerUser(request.toCreateUserRequest(response.getId()));
    }

    @Override
    public Long login(LoginRequest request) {
        GoogleProfileInfoResponse response = googleAuthApiClient.getProfileInfo((HttpHeaderUtils.withBearerToken(request.getToken())));
        return UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, response.getId(), socialType).getId();
    }

}
