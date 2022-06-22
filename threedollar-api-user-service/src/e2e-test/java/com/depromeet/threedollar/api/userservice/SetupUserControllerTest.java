package com.depromeet.threedollar.api.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.userservice.controller.user.UserMockApiCaller;
import com.depromeet.threedollar.api.userservice.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;

public abstract class SetupUserControllerTest extends ControllerTest {

    @Autowired
    protected UserRepository userRepository;

    protected User user;

    protected String token;

    @BeforeEach
    void setupUser() throws Exception {
        UserMockApiCaller userMockApiCaller = new UserMockApiCaller(mockMvc, objectMapper);
        LoginResponse response = userMockApiCaller.getTestToken().getData();
        user = userRepository.findUserById(response.getUserId());
        token = response.getToken();
    }

}
