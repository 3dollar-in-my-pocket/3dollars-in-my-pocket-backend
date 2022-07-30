package com.depromeet.threedollar.api.core.service.service.userservice;

import com.depromeet.threedollar.api.core.service.IntegrationTest;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

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
