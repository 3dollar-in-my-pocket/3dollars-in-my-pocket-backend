package com.depromeet.threedollar.api.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.user.controller.user.UserMockApiCaller;
import com.depromeet.threedollar.api.user.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionRepository;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.WithdrawalUserRepository;

public abstract class SetupUserControllerTest extends SetupControllerTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserMedalRepository userMedalRepository;

    @Autowired
    protected MedalRepository medalRepository;

    @Autowired
    protected MedalAcquisitionConditionRepository medalAcquisitionConditionRepository;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    protected User user;

    protected String token;

    @BeforeEach
    void setupUser() throws Exception {
        UserMockApiCaller userMockApiCaller = new UserMockApiCaller(mockMvc, objectMapper);
        LoginResponse response = userMockApiCaller.getTestToken().getData();
        user = userRepository.findUserById(response.getUserId());
        token = response.getToken();
    }

    protected void cleanup() {
        medalAcquisitionConditionRepository.deleteAllInBatch();
        userMedalRepository.deleteAllInBatch();
        medalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        withdrawalUserRepository.deleteAllInBatch();
    }

}
