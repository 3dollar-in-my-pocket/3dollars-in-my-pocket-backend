package com.depromeet.threedollar.statistics

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@ContextConfiguration(initializers = [ElasticsearchContextInitializer::class])
@SpringBootTest
internal class ThreeDollarStatisticsApplicationKtTest {

    @Test
    fun contextsLoad() {

    }

}
