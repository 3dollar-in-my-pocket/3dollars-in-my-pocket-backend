package com.depromeet.threedollar.api.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;

public abstract class SetupUserIntegrationTest extends IntegrationTest {

    @Autowired
    protected UserRepository userRepository;

    protected Long userId;

    protected User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserFixture.create("social-id", UserSocialType.KAKAO, "디프만"));
        userId = user.getId();
    }

}
