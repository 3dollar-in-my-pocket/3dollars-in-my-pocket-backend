package com.depromeet.threedollar.api.vendor.service.auth;

import com.depromeet.threedollar.api.vendor.service.auth.dto.request.LoginRequest;
import com.depromeet.threedollar.api.vendor.service.auth.dto.request.SignUpRequest;

public interface AuthService {

    Long signUp(SignUpRequest request);

    Long login(LoginRequest request);

}
