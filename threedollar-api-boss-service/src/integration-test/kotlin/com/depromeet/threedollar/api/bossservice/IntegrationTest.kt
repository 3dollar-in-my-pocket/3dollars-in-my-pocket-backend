package com.depromeet.threedollar.api.bossservice

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.depromeet.threedollar.domain.rds.domain.lib.DatabaseCleaner

@SpringBootTest
abstract class IntegrationTest {

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    @AfterEach
    fun cleanup() {
        databaseCleaner.cleanUp()
    }

}
