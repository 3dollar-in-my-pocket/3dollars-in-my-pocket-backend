package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.user.request.LoginRequest;
import com.depromeet.threedollar.api.service.user.request.SignUpRequest;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.external.client.apple.AppleTokenDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AppleAuthService implements AuthService {

    private static final UserSocialType socialType = UserSocialType.APPLE;

    private final AppleTokenDecoder appleTokenDecoder;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    @Override
    public Long signUp(SignUpRequest request) {
        String socialId = appleTokenDecoder.getUserIdFromToken(request.getToken());
        return userService.createUser(request.toCreateUserRequest(socialId));
    }

    @Transactional
    @Override
    public Long login(LoginRequest request) {
        String socialId = appleTokenDecoder.getUserIdFromToken(request.getToken());
        return UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, socialId, socialType).getId();
    }

}
