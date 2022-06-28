package com.depromeet.threedollar.api.core

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.domain.rds.domain.lib.DatabaseCleaner

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
abstract class IntegrationTest {

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    @AfterEach
    fun cleanup() {
        databaseCleaner.cleanUp()
    }

}