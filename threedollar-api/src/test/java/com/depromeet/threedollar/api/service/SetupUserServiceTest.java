package com.depromeet.threedollar.api.service;

import com.depromeet.threedollar.domain.domain.medal.MedalAcqusitionConditionRepository;
import com.depromeet.threedollar.domain.domain.medal.MedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserCreator;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SetupUserServiceTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserMedalRepository userMedalRepository;

    @Autowired
    protected MedalRepository medalRepository;

    @Autowired
    protected MedalAcqusitionConditionRepository medalAcqusitionConditionRepository;

    protected Long userId;

    @BeforeEach
    void setup() {
        User user = userRepository.save(UserCreator.create("social-id", UserSocialType.KAKAO, "디프만"));
        userId = user.getId();
    }

    protected void cleanup() {
        medalAcqusitionConditionRepository.deleteAllInBatch();
        userMedalRepository.deleteAllInBatch();
        medalRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

}
