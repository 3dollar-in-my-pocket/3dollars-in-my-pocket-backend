package com.depromeet.threedollar.statistics.initializer

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.elasticsearch.ElasticsearchContainer

class ElasticsearchContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        elasticsearch.start()
        TestPropertyValues.of(
            "spring.elasticsearch.rest.uris=${elasticsearch.httpHostAddress}"
        ).applyTo(context.environment)
    }

    companion object {
        private const val ELASTIC_SEARCH_DOCKER_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch-oss:7.10.1"
        val elasticsearch: ElasticsearchContainer = ElasticsearchContainer(ELASTIC_SEARCH_DOCKER_IMAGE)
            .withEnv("discovery.type", "single-node")
    }

}
