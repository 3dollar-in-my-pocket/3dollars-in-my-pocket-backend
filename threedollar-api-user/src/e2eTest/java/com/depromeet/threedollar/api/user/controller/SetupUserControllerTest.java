package com.depromeet.threedollar.api.user.controller;

import com.depromeet.threedollar.api.user.controller.user.UserMockApiCaller;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionRepository;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.user.domain.user.WithdrawalUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SetupUserControllerTest extends SetupControllerTest {

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

    protected User user;

    protected String token;

    @BeforeEach
    void setupUser() throws Exception {
        UserMockApiCaller userMockApiCaller = new UserMockApiCaller(mockMvc, objectMapper);
        token = userMockApiCaller.getTestToken().getData().getToken();
        user = userRepository.findUserBySocialIdAndSocialType("test-uid", UserSocialType.KAKAO);
    }

    protected void cleanup() {
        medalAcquisitionConditionRepository.deleteAllInBatch();
        userMedalRepository.deleteAllInBatch();
        medalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        withdrawalUserRepository.deleteAllInBatch();
    }

}
