package com.depromeet.threedollar.statistics

import com.depromeet.threedollar.statistics.initializer.ElasticsearchContextInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9095", "port=9095"])
@ContextConfiguration(initializers = [ElasticsearchContextInitializer::class])
@SpringBootTest
abstract class BaseIntegrationTest
