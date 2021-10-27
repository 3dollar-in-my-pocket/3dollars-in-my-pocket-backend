package com.depromeet.threedollar.api.service.auth;

import com.depromeet.threedollar.api.service.user.request.LoginRequest;
import com.depromeet.threedollar.api.service.user.request.SignUpRequest;

public interface AuthService {

    Long signUp(SignUpRequest request);

    Long login(LoginRequest request);

}
