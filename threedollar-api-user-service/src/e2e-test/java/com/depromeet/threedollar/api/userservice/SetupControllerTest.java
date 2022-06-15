package com.depromeet.threedollar.api.userservice;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import com.depromeet.threedollar.domain.rds.domain.lib.DatabaseCleaner;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
public abstract class SetupControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanup() {
        databaseCleaner.cleanUp();
    }

}
