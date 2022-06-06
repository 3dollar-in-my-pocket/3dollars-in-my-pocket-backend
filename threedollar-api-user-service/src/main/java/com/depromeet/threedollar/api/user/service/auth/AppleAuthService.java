package com.depromeet.threedollar.api.user.service.auth;

import org.springframework.stereotype.Service;

import com.depromeet.threedollar.api.user.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.user.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.user.service.user.UserService;
import com.depromeet.threedollar.api.user.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
class AppleAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.APPLE;

    private final AppleTokenDecoder appleTokenDecoder;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public Long signUp(SignUpRequest request) {
        String socialId = appleTokenDecoder.getSocialIdFromIdToken(request.getToken());
        return userService.registerUser(request.toCreateUserRequest(socialId));
    }

    @Override
    public Long login(LoginRequest request) {
        String socialId = appleTokenDecoder.getSocialIdFromIdToken(request.getToken());
        return UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, socialId, socialType).getId();
    }

}
