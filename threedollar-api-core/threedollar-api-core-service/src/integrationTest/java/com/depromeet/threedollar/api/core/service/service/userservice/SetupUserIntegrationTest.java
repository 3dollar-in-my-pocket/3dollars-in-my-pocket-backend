package com.depromeet.threedollar.api.core.service.service.userservice;

import com.depromeet.threedollar.api.core.service.IntegrationTest;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SetupUserIntegrationTest extends IntegrationTest {

    @Autowired
    protected UserRepository userRepository;

    protected Long userId;

    protected User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserFixture.create());
        userId = user.getId();
    }

}
