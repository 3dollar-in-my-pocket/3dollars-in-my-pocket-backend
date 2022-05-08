package com.depromeet.threedollar.api.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionRepository;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.User;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserCreator;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;

@SpringBootTest
public abstract class SetupUserServiceTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserMedalRepository userMedalRepository;

    @Autowired
    protected MedalRepository medalRepository;

    @Autowired
    protected MedalAcquisitionConditionRepository medalAcquisitionConditionRepository;

    protected Long userId;

    protected User user;

    @BeforeEach
    void setup() {
        user = UserCreator.builder()
            .socialId("social-id")
            .name("디프만")
            .socialType(UserSocialType.KAKAO)
            .build();
        userRepository.save(user);
        userId = user.getId();
    }

    protected void cleanup() {
        medalAcquisitionConditionRepository.deleteAllInBatch();
        userMedalRepository.deleteAllInBatch();
        medalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

}
