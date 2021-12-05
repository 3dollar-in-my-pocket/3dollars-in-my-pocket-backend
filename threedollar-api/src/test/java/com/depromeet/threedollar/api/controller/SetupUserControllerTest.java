package com.depromeet.threedollar.api.controller;

import com.depromeet.threedollar.api.controller.user.UserMockApiCaller;
import com.depromeet.threedollar.domain.domain.medal.MedalAcquisitionConditionRepository;
import com.depromeet.threedollar.domain.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.domain.user.WithdrawalUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public abstract class SetupUserControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private WithdrawalUserRepository withdrawalUserRepository;

    @Autowired
    protected UserMedalRepository userMedalRepository;

    @Autowired
    protected MedalRepository medalRepository;

    @Autowired
    protected MedalAcquisitionConditionRepository medalAcquisitionConditionRepository;

    protected User testUser;

    protected String token;

    @BeforeEach
    void setupUser() throws Exception {
        UserMockApiCaller userMockApiCaller = new UserMockApiCaller(mockMvc, objectMapper);
        token = userMockApiCaller.getTestToken().getData().getToken();
        testUser = userRepository.findUserBySocialIdAndSocialType("test-uid", UserSocialType.KAKAO);
    }

    protected void cleanup() {
        medalAcquisitionConditionRepository.deleteAllInBatch();
        userMedalRepository.deleteAllInBatch();
        medalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        withdrawalUserRepository.deleteAllInBatch();
    }

}
