package com.depromeet.threedollar.api.userservice;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.domain.rds.domain.lib.DatabaseCleaner;

@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanup() {
        databaseCleaner.cleanUp();
    }

}
