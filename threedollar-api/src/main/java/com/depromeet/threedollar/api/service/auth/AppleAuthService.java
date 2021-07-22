package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.service.auth.dto.request.SignUpRequest;
import com.depromeet.threedollar.api.service.user.UserService;
import com.depromeet.threedollar.api.service.user.UserServiceUtils;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.external.external.auth.apple.AppleTokenDecoder;
import com.depromeet.threedollar.external.external.auth.apple.dto.response.IdTokenPayload;
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
        IdTokenPayload payload = appleTokenDecoder.getUserInfoFromToken(request.getToken());
        return userService.createUser(request.toCreateUserRequest(payload.getSub()));
    }

    @Transactional
    @Override
    public Long login(LoginRequest request) {
        IdTokenPayload payload = appleTokenDecoder.getUserInfoFromToken(request.getToken());
        return UserServiceUtils.findUserBySocialIdAndSocialType(userRepository, payload.getSub(), socialType).getId();
    }

    @Transactional
    @Override
    public void signOut(Long userId) {
        userService.signOut(userId, socialType);
    }

}
