package com.depromeet.threedollar.api.bossservice

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import com.depromeet.threedollar.domain.rds.domain.lib.DatabaseCleaner
import com.fasterxml.jackson.databind.ObjectMapper

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal abstract class SetupControllerTest {

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    @AfterEach
    fun cleanup() {
        databaseCleaner.cleanUp()
    }

}
